package br.com.goods.Goods.Investimentos.services;

import br.com.goods.Goods.Investimentos.mappers.LancamentoEtfInterMapper;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoEtfInterRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoEtfInterResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoEtfInterEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoEtfInterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LancamentoEtfInterService {

    private final LancamentoEtfInterRepository repository;
    private final LancamentoEtfInterMapper mapper;

    @Autowired
    public LancamentoEtfInterService(LancamentoEtfInterRepository repository, LancamentoEtfInterMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<LancamentoEtfInterResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LancamentoEtfInterResponseDTO findById(Long id) {
        LancamentoEtfInterEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com ID: " + id));
        return mapper.toResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<LancamentoEtfInterResponseDTO> findByIdUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoEtfInterResponseDTO> findByIdUsuarioAndTimeBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate) {
        return repository.findByIdUsuarioAndTimeBetween(idUsuario, startDate, endDate).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoEtfInterResponseDTO> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo) {
        return repository.findByIdUsuarioAndAtivo(idUsuario, ativo).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LancamentoEtfInterResponseDTO create(LancamentoEtfInterRequestDTO requestDTO) {
        LancamentoEtfInterEntity entity = mapper.toEntity(requestDTO);
        LancamentoEtfInterEntity savedEntity = repository.save(entity);
        return mapper.toResponseDTO(savedEntity);
    }

    @Transactional
    public LancamentoEtfInterResponseDTO update(Long id, LancamentoEtfInterRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }

        LancamentoEtfInterEntity entity = mapper.toEntity(requestDTO);
        entity.setId(id);
        LancamentoEtfInterEntity updatedEntity = repository.save(entity);
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
