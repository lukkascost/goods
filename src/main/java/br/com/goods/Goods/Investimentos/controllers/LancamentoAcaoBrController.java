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

    @GetMapping("/{time}")
    public ResponseEntity<LancamentoAcaoBrResponseDTO> findById(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate time) {
        try {
            return ResponseEntity.ok(service.findById(time));
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

    @PutMapping("/{time}")
    public ResponseEntity<LancamentoAcaoBrResponseDTO> update(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate time,
            @Valid @RequestBody LancamentoAcaoBrRequestDTO requestDTO) {
        try {
            LancamentoAcaoBrResponseDTO responseDTO = service.update(time, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{time}")
    public ResponseEntity<Void> delete(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate time) {
        try {
            service.delete(time);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
