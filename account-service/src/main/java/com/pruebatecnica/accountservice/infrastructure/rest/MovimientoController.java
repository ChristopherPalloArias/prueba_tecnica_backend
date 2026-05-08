package com.pruebatecnica.accountservice.infrastructure.rest;

import com.pruebatecnica.accountservice.application.dto.EstadoRequest;
import com.pruebatecnica.accountservice.application.dto.MovimientoCreateRequest;
import com.pruebatecnica.accountservice.application.dto.MovimientoResponse;
import com.pruebatecnica.accountservice.application.dto.MovimientoUpdateRequest;
import com.pruebatecnica.accountservice.application.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> create(@Valid @RequestBody MovimientoCreateRequest request) {
        MovimientoResponse response = movimientoService.create(request);
        return ResponseEntity.created(URI.create("/movimientos/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> findAll(
            @RequestParam(defaultValue = "false") boolean incluirInactivos
    ) {
        return ResponseEntity.ok(movimientoService.findAll(incluirInactivos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MovimientoUpdateRequest request
    ) {
        return ResponseEntity.ok(movimientoService.update(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<MovimientoResponse> updateEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequest request
    ) {
        return ResponseEntity.ok(movimientoService.updateEstado(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
