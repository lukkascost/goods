package br.com.goods.Goods.Investimentos.services;

import br.com.goods.Goods.Investimentos.mappers.LancamentoFiiBrMapper;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoFiiBrRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoFiiBrResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoFiiBrEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoFiiBrRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LancamentoFiiBrService {

    private final LancamentoFiiBrRepository repository;
    private final LancamentoFiiBrMapper mapper;

    @Autowired
    public LancamentoFiiBrService(LancamentoFiiBrRepository repository, LancamentoFiiBrMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<LancamentoFiiBrResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LancamentoFiiBrResponseDTO findById(Long id) {
        LancamentoFiiBrEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com ID: " + id));
        return mapper.toResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<LancamentoFiiBrResponseDTO> findByIdUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoFiiBrResponseDTO> findByIdUsuarioAndTimeBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate) {
        return repository.findByIdUsuarioAndTimeBetween(idUsuario, startDate, endDate).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoFiiBrResponseDTO> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo) {
        return repository.findByIdUsuarioAndAtivo(idUsuario, ativo).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LancamentoFiiBrResponseDTO create(LancamentoFiiBrRequestDTO requestDTO) {
        LancamentoFiiBrEntity entity = mapper.toEntity(requestDTO);
        LancamentoFiiBrEntity savedEntity = repository.save(entity);
        return mapper.toResponseDTO(savedEntity);
    }

    @Transactional
    public LancamentoFiiBrResponseDTO update(Long id, LancamentoFiiBrRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }

        LancamentoFiiBrEntity entity = mapper.toEntity(requestDTO);
        entity.setId(id);
        LancamentoFiiBrEntity updatedEntity = repository.save(entity);
        return mapper.toResponseDTO(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
