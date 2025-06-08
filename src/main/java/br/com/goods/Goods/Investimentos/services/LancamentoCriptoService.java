package br.com.goods.Goods.Investimentos.services;

import br.com.goods.Goods.Investimentos.mappers.LancamentoCriptoMapper;
import br.com.goods.Goods.Investimentos.mappers.LancamentoCriptoFilterMapper;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoFilterDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoCriptoEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoCriptoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;

@Service
public class LancamentoCriptoService {

    private final LancamentoCriptoRepository repository;
    private final LancamentoCriptoMapper mapper;
    private final LancamentoCriptoFilterMapper filterMapper;

    @Autowired
    public LancamentoCriptoService(LancamentoCriptoRepository repository,
                                   LancamentoCriptoMapper mapper,
                                   LancamentoCriptoFilterMapper filterMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.filterMapper = filterMapper;
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findAll(LancamentoCriptoFilterDTO filterDTO) {
        Specification<LancamentoCriptoEntity> spec = filterMapper.toSpecification(filterDTO);

        return repository.findAll(spec).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findAll() {
        return findAll(null);
    }

    @Transactional(readOnly = true)
    public LancamentoCriptoResponseDTO findById(Long id) {
        LancamentoCriptoEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com ID: " + id));
        return mapper.toResponseDTO(entity);
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
