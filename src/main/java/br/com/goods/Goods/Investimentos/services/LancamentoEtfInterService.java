package br.com.goods.Goods.Investimentos.services;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoEtfInterRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoEtfInterResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoEtfInterEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoEtfInterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LancamentoEtfInterService {

    private final LancamentoEtfInterRepository repository;

    @Autowired
    public LancamentoEtfInterService(LancamentoEtfInterRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<LancamentoEtfInterResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LancamentoEtfInterResponseDTO findById(Long id) {
        LancamentoEtfInterEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com ID: " + id));
        return convertToResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<LancamentoEtfInterResponseDTO> findByIdUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoEtfInterResponseDTO> findByIdUsuarioAndTimeBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate) {
        return repository.findByIdUsuarioAndTimeBetween(idUsuario, startDate, endDate).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoEtfInterResponseDTO> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo) {
        return repository.findByIdUsuarioAndAtivo(idUsuario, ativo).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LancamentoEtfInterResponseDTO create(LancamentoEtfInterRequestDTO requestDTO) {
        LancamentoEtfInterEntity entity = convertToEntity(requestDTO);
        LancamentoEtfInterEntity savedEntity = repository.save(entity);
        return convertToResponseDTO(savedEntity);
    }

    @Transactional
    public LancamentoEtfInterResponseDTO update(Long id, LancamentoEtfInterRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }
        
        LancamentoEtfInterEntity entity = convertToEntity(requestDTO);
        entity.setId(id);
        LancamentoEtfInterEntity updatedEntity = repository.save(entity);
        return convertToResponseDTO(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private LancamentoEtfInterEntity convertToEntity(LancamentoEtfInterRequestDTO requestDTO) {
        LancamentoEtfInterEntity entity = new LancamentoEtfInterEntity();
        BeanUtils.copyProperties(requestDTO, entity);
        return entity;
    }

    private LancamentoEtfInterResponseDTO convertToResponseDTO(LancamentoEtfInterEntity entity) {
        LancamentoEtfInterResponseDTO responseDTO = new LancamentoEtfInterResponseDTO();
        BeanUtils.copyProperties(entity, responseDTO);
        return responseDTO;
    }
}