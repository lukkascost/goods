package br.com.goods.Goods.Investimentos.services;

import br.com.goods.Goods.Investimentos.mappers.LancamentoCriptoMapper;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoCriptoEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoCriptoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LancamentoCriptoService {

    private final LancamentoCriptoRepository repository;
    private final LancamentoCriptoMapper mapper;

    @Autowired
    public LancamentoCriptoService(LancamentoCriptoRepository repository, LancamentoCriptoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LancamentoCriptoResponseDTO findById(Long id) {
        LancamentoCriptoEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com ID: " + id));
        return mapper.toResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findByIdUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findByIdUsuarioAndTimeBetween(Integer idUsuario, ZonedDateTime startDate, ZonedDateTime endDate) {
        return repository.findByIdUsuarioAndTimeBetween(idUsuario, startDate, endDate).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo) {
        return repository.findByIdUsuarioAndAtivo(idUsuario, ativo).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LancamentoCriptoResponseDTO create(LancamentoCriptoRequestDTO requestDTO) {
        LancamentoCriptoEntity entity = mapper.toEntity(requestDTO);
        LancamentoCriptoEntity savedEntity = repository.save(entity);
        return mapper.toResponseDTO(savedEntity);
    }

    @Transactional
    public LancamentoCriptoResponseDTO update(Long id, LancamentoCriptoRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }

        LancamentoCriptoEntity entity = mapper.toEntity(requestDTO);
        entity.setId(id);
        LancamentoCriptoEntity updatedEntity = repository.save(entity);
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
