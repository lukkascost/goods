package br.com.goods.Goods.Investimentos.controllers;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoAcaoBrRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoAcaoBrResponseDTO;
import br.com.goods.Goods.Investimentos.services.LancamentoAcaoBrService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lancamentos/acoes-br")
public class LancamentoAcaoBrController {

    private final LancamentoAcaoBrService service;

    @Autowired
    public LancamentoAcaoBrController(LancamentoAcaoBrService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LancamentoAcaoBrResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LancamentoAcaoBrResponseDTO> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<LancamentoAcaoBrResponseDTO>> findByIdUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(service.findByIdUsuario(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/periodo")
    public ResponseEntity<List<LancamentoAcaoBrResponseDTO>> findByIdUsuarioAndTimeBetween(
            @PathVariable Integer idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(service.findByIdUsuarioAndTimeBetween(idUsuario, startDate, endDate));
    }

    @GetMapping("/usuario/{idUsuario}/ativo/{ativo}")
    public ResponseEntity<List<LancamentoAcaoBrResponseDTO>> findByIdUsuarioAndAtivo(
            @PathVariable Integer idUsuario,
            @PathVariable String ativo) {
        return ResponseEntity.ok(service.findByIdUsuarioAndAtivo(idUsuario, ativo));
    }

    @PostMapping
    public ResponseEntity<LancamentoAcaoBrResponseDTO> create(@Valid @RequestBody LancamentoAcaoBrRequestDTO requestDTO) {
        LancamentoAcaoBrResponseDTO responseDTO = service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LancamentoAcaoBrResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LancamentoAcaoBrRequestDTO requestDTO) {
        try {
            LancamentoAcaoBrResponseDTO responseDTO = service.update(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}