package br.com.goods.Goods.Investimentos.services;

import br.com.goods.Goods.Investimentos.mappers.LancamentoAcaoBrMapper;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoAcaoBrRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoAcaoBrResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoAcaoBrEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoAcaoBrRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LancamentoAcaoBrService {

    private final LancamentoAcaoBrRepository repository;
    private final LancamentoAcaoBrMapper mapper;

    @Autowired
    public LancamentoAcaoBrService(LancamentoAcaoBrRepository repository, LancamentoAcaoBrMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public LancamentoAcaoBrResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com o id: " + id));
    }

    @Transactional(readOnly = true)
    public List<LancamentoAcaoBrResponseDTO> findByIdUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoAcaoBrResponseDTO> findByIdUsuarioAndTimeBetween(Integer idUsuario, ZonedDateTime startDate, ZonedDateTime endDate) {
        return repository.findByIdUsuarioAndTimeBetween(idUsuario, startDate, endDate).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoAcaoBrResponseDTO> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo) {
        return repository.findByIdUsuarioAndAtivo(idUsuario, ativo).stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LancamentoAcaoBrResponseDTO create(LancamentoAcaoBrRequestDTO requestDTO) {
        LancamentoAcaoBrEntity entity = mapper.toEntity(requestDTO);
        LancamentoAcaoBrEntity savedEntity = repository.save(entity);
        return mapper.toResponseDTO(savedEntity);
    }

    @Transactional
    public LancamentoAcaoBrResponseDTO update(Long id, LancamentoAcaoBrRequestDTO requestDTO) {
        LancamentoAcaoBrEntity existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com o id: " + id));

        LancamentoAcaoBrEntity entity = mapper.toEntity(requestDTO);
        entity.setId(id); // Ensure we're updating the correct entity
        entity.setTime(existingEntity.getTime()); // Preserve the original time
        LancamentoAcaoBrEntity savedEntity = repository.save(entity);
        return mapper.toResponseDTO(savedEntity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com o id: " + id);
        }
        repository.deleteById(id);
    }
}
