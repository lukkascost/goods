package br.com.goods.Goods.Investimentos.services;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoFiiBrRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoFiiBrResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoFiiBrEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoFiiBrRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LancamentoFiiBrService {

    private final LancamentoFiiBrRepository repository;

    @Autowired
    public LancamentoFiiBrService(LancamentoFiiBrRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<LancamentoFiiBrResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LancamentoFiiBrResponseDTO findById(Long id) {
        LancamentoFiiBrEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com ID: " + id));
        return convertToResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<LancamentoFiiBrResponseDTO> findByIdUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoFiiBrResponseDTO> findByIdUsuarioAndTimeBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate) {
        return repository.findByIdUsuarioAndTimeBetween(idUsuario, startDate, endDate).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoFiiBrResponseDTO> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo) {
        return repository.findByIdUsuarioAndAtivo(idUsuario, ativo).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LancamentoFiiBrResponseDTO create(LancamentoFiiBrRequestDTO requestDTO) {
        LancamentoFiiBrEntity entity = convertToEntity(requestDTO);
        LancamentoFiiBrEntity savedEntity = repository.save(entity);
        return convertToResponseDTO(savedEntity);
    }

    @Transactional
    public LancamentoFiiBrResponseDTO update(Long id, LancamentoFiiBrRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }
        
        LancamentoFiiBrEntity entity = convertToEntity(requestDTO);
        entity.setId(id);
        LancamentoFiiBrEntity updatedEntity = repository.save(entity);
        return convertToResponseDTO(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private LancamentoFiiBrEntity convertToEntity(LancamentoFiiBrRequestDTO requestDTO) {
        LancamentoFiiBrEntity entity = new LancamentoFiiBrEntity();
        BeanUtils.copyProperties(requestDTO, entity);
        return entity;
    }

    private LancamentoFiiBrResponseDTO convertToResponseDTO(LancamentoFiiBrEntity entity) {
        LancamentoFiiBrResponseDTO responseDTO = new LancamentoFiiBrResponseDTO();
        BeanUtils.copyProperties(entity, responseDTO);
        return responseDTO;
    }
}