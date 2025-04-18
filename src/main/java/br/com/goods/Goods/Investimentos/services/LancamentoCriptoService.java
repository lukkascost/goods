package br.com.goods.Goods.Investimentos.services;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoCriptoEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoCriptoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LancamentoCriptoService {

    private final LancamentoCriptoRepository repository;

    @Autowired
    public LancamentoCriptoService(LancamentoCriptoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LancamentoCriptoResponseDTO findById(Long id) {
        LancamentoCriptoEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com ID: " + id));
        return convertToResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findByIdUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findByIdUsuarioAndTimeBetween(Integer idUsuario, ZonedDateTime startDate, ZonedDateTime endDate) {
        return repository.findByIdUsuarioAndTimeBetween(idUsuario, startDate, endDate).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo) {
        return repository.findByIdUsuarioAndAtivo(idUsuario, ativo).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LancamentoCriptoResponseDTO create(LancamentoCriptoRequestDTO requestDTO) {
        LancamentoCriptoEntity entity = convertToEntity(requestDTO);
        LancamentoCriptoEntity savedEntity = repository.save(entity);
        return convertToResponseDTO(savedEntity);
    }

    @Transactional
    public LancamentoCriptoResponseDTO update(Long id, LancamentoCriptoRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }

        LancamentoCriptoEntity entity = convertToEntity(requestDTO);
        entity.setId(id);
        LancamentoCriptoEntity updatedEntity = repository.save(entity);
        return convertToResponseDTO(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private LancamentoCriptoEntity convertToEntity(LancamentoCriptoRequestDTO requestDTO) {
        LancamentoCriptoEntity entity = new LancamentoCriptoEntity();
        BeanUtils.copyProperties(requestDTO, entity);
        return entity;
    }

    private LancamentoCriptoResponseDTO convertToResponseDTO(LancamentoCriptoEntity entity) {
        LancamentoCriptoResponseDTO responseDTO = new LancamentoCriptoResponseDTO();
        BeanUtils.copyProperties(entity, responseDTO);
        return responseDTO;
    }
}
