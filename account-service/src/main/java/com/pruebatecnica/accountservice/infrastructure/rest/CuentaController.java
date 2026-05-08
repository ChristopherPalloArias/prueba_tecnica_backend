package com.pruebatecnica.accountservice.infrastructure.rest;

import com.pruebatecnica.accountservice.application.dto.CuentaCreateRequest;
import com.pruebatecnica.accountservice.application.dto.CuentaResponse;
import com.pruebatecnica.accountservice.application.dto.CuentaUpdateRequest;
import com.pruebatecnica.accountservice.application.dto.EstadoRequest;
import com.pruebatecnica.accountservice.application.service.CuentaService;
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
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> create(@Valid @RequestBody CuentaCreateRequest request) {
        CuentaResponse response = cuentaService.create(request);
        return ResponseEntity.created(URI.create("/cuentas/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> findAll(
            @RequestParam(defaultValue = "false") boolean incluirInactivas
    ) {
        return ResponseEntity.ok(cuentaService.findAll(incluirInactivas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CuentaUpdateRequest request
    ) {
        return ResponseEntity.ok(cuentaService.update(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CuentaResponse> updateEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequest request
    ) {
        return ResponseEntity.ok(cuentaService.updateEstado(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
