package br.com.goods.Goods.Investimentos.services;

import br.com.goods.Goods.Investimentos.mappers.LancamentoRendaFixaMapper;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoRendaFixaRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoRendaFixaResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoRendaFixaEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoRendaFixaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LancamentoRendaFixaService {

    private final LancamentoRendaFixaRepository repository;
    private final LancamentoRendaFixaMapper mapper;

    @Autowired
    public LancamentoRendaFixaService(LancamentoRendaFixaRepository repository, LancamentoRendaFixaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<LancamentoRendaFixaResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LancamentoRendaFixaResponseDTO findById(Long id) {
        LancamentoRendaFixaEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com ID: " + id));
        return convertToResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<LancamentoRendaFixaResponseDTO> findByIdUsuario(Integer idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoRendaFixaResponseDTO> findByIdUsuarioAndDataDeCompraBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate) {
        return repository.findByIdUsuarioAndDataDeCompraBetween(idUsuario, startDate, endDate).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoRendaFixaResponseDTO> findByIdUsuarioAndTipoDeTitulo(Integer idUsuario, String tipoDeTitulo) {
        return repository.findByIdUsuarioAndTipoDeTitulo(idUsuario, tipoDeTitulo).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LancamentoRendaFixaResponseDTO> findByIdUsuarioAndEmissor(Integer idUsuario, String emissor) {
        return repository.findByIdUsuarioAndEmissor(idUsuario, emissor).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LancamentoRendaFixaResponseDTO create(LancamentoRendaFixaRequestDTO requestDTO) {
        LancamentoRendaFixaEntity entity = convertToEntity(requestDTO);
        LancamentoRendaFixaEntity savedEntity = repository.save(entity);
        return convertToResponseDTO(savedEntity);
    }

    @Transactional
    public LancamentoRendaFixaResponseDTO update(Long id, LancamentoRendaFixaRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }

        LancamentoRendaFixaEntity entity = convertToEntity(requestDTO);
        entity.setId(id);
        LancamentoRendaFixaEntity updatedEntity = repository.save(entity);
        return convertToResponseDTO(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private LancamentoRendaFixaEntity convertToEntity(LancamentoRendaFixaRequestDTO requestDTO) {
        LancamentoRendaFixaEntity entity = new LancamentoRendaFixaEntity();
        BeanUtils.copyProperties(requestDTO, entity);
        return entity;
    }

    private LancamentoRendaFixaResponseDTO convertToResponseDTO(LancamentoRendaFixaEntity entity) {
        LancamentoRendaFixaResponseDTO responseDTO = new LancamentoRendaFixaResponseDTO();
        BeanUtils.copyProperties(entity, responseDTO);
        return responseDTO;
    }
}
