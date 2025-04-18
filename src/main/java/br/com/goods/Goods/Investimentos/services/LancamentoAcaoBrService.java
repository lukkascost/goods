package br.com.goods.Goods.Investimentos.services;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoAcaoBrRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoAcaoBrResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoAcaoBrEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoAcaoBrRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LancamentoAcaoBrService {

    private final LancamentoAcaoBrRepository repository;

    @Autowired
    public LancamentoAcaoBrService(LancamentoAcaoBrRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<LancamentoAcaoBrResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LancamentoAcaoBrResponseDTO findById(ZonedDateTime time) {
        return repository.findById(time)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com a data: " + time));
    }

    @Transactional(readOnly = true)
    public List<LancamentoAcaoBrResponseDTO> findByIdUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoAcaoBrResponseDTO> findByIdUsuarioAndTimeBetween(Integer idUsuario, ZonedDateTime startDate, ZonedDateTime endDate) {
        return repository.findByIdUsuarioAndTimeBetween(idUsuario, startDate, endDate).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoAcaoBrResponseDTO> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo) {
        return repository.findByIdUsuarioAndAtivo(idUsuario, ativo).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LancamentoAcaoBrResponseDTO create(LancamentoAcaoBrRequestDTO requestDTO) {
        LancamentoAcaoBrEntity entity = convertToEntity(requestDTO);
        LancamentoAcaoBrEntity savedEntity = repository.save(entity);
        return convertToResponseDTO(savedEntity);
    }

    @Transactional
    public LancamentoAcaoBrResponseDTO update(ZonedDateTime time, LancamentoAcaoBrRequestDTO requestDTO) {
        if (!repository.existsById(time)) {
            throw new EntityNotFoundException("Lançamento não encontrado com a data: " + time);
        }

        LancamentoAcaoBrEntity entity = convertToEntity(requestDTO);
        entity.setTime(time); // Ensure we're updating the correct entity
        LancamentoAcaoBrEntity savedEntity = repository.save(entity);
        return convertToResponseDTO(savedEntity);
    }

    @Transactional
    public void delete(ZonedDateTime time) {
        if (!repository.existsById(time)) {
            throw new EntityNotFoundException("Lançamento não encontrado com a data: " + time);
        }
        repository.deleteById(time);
    }

    private LancamentoAcaoBrEntity convertToEntity(LancamentoAcaoBrRequestDTO requestDTO) {
        LancamentoAcaoBrEntity entity = new LancamentoAcaoBrEntity();
        BeanUtils.copyProperties(requestDTO, entity);
        return entity;
    }

    private LancamentoAcaoBrResponseDTO convertToResponseDTO(LancamentoAcaoBrEntity entity) {
        LancamentoAcaoBrResponseDTO responseDTO = new LancamentoAcaoBrResponseDTO();
        BeanUtils.copyProperties(entity, responseDTO);
        return responseDTO;
    }
}
