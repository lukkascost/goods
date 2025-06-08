package br.com.goods.Goods.Investimentos.controllers;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoFilterDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoResponseDTO;
import br.com.goods.Goods.Investimentos.services.LancamentoCriptoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lancamentos/cripto")
public class LancamentoCriptoController {

    private final LancamentoCriptoService service;

    @Autowired
    public LancamentoCriptoController(LancamentoCriptoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LancamentoCriptoResponseDTO> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<LancamentoCriptoResponseDTO>> findAll(LancamentoCriptoFilterDTO filterDTO) {
        return ResponseEntity.ok(service.findAll(filterDTO));
    }

    @PostMapping
    public ResponseEntity<LancamentoCriptoResponseDTO> create(@Valid @RequestBody LancamentoCriptoRequestDTO requestDTO) {
        LancamentoCriptoResponseDTO responseDTO = service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LancamentoCriptoResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LancamentoCriptoRequestDTO requestDTO) {
        try {
            LancamentoCriptoResponseDTO responseDTO = service.update(id, requestDTO);
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
