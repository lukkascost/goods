package br.com.goods.Goods.Investimentos.controllers;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoRendaFixaRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoRendaFixaResponseDTO;
import br.com.goods.Goods.Investimentos.services.LancamentoRendaFixaService;
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
@RequestMapping("/api/lancamentos/renda-fixa")
public class LancamentoRendaFixaController {

    private final LancamentoRendaFixaService service;

    @Autowired
    public LancamentoRendaFixaController(LancamentoRendaFixaService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public ResponseEntity<LancamentoRendaFixaResponseDTO> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<LancamentoRendaFixaResponseDTO>> findByIdUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(service.findByIdUsuario(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/periodo")
    public ResponseEntity<List<LancamentoRendaFixaResponseDTO>> findByIdUsuarioAndDataDeCompraBetween(
            @PathVariable Integer idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(service.findByIdUsuarioAndDataDeCompraBetween(idUsuario, startDate, endDate));
    }

    @GetMapping("/usuario/{idUsuario}/tipo/{tipoDeTitulo}")
    public ResponseEntity<List<LancamentoRendaFixaResponseDTO>> findByIdUsuarioAndTipoDeTitulo(
            @PathVariable Integer idUsuario,
            @PathVariable String tipoDeTitulo) {
        return ResponseEntity.ok(service.findByIdUsuarioAndTipoDeTitulo(idUsuario, tipoDeTitulo));
    }

    @GetMapping("/usuario/{idUsuario}/emissor/{emissor}")
    public ResponseEntity<List<LancamentoRendaFixaResponseDTO>> findByIdUsuarioAndEmissor(
            @PathVariable Integer idUsuario,
            @PathVariable String emissor) {
        return ResponseEntity.ok(service.findByIdUsuarioAndEmissor(idUsuario, emissor));
    }

    @PostMapping
    public ResponseEntity<LancamentoRendaFixaResponseDTO> create(@Valid @RequestBody LancamentoRendaFixaRequestDTO requestDTO) {
        LancamentoRendaFixaResponseDTO responseDTO = service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LancamentoRendaFixaResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LancamentoRendaFixaRequestDTO requestDTO) {
        try {
            LancamentoRendaFixaResponseDTO responseDTO = service.update(id, requestDTO);
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