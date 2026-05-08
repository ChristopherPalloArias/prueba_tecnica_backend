package com.pruebatecnica.customerservice.infrastructure.rest;

import com.pruebatecnica.customerservice.application.dto.ClienteCreateRequest;
import com.pruebatecnica.customerservice.application.dto.ClienteEstadoRequest;
import com.pruebatecnica.customerservice.application.dto.ClienteResponse;
import com.pruebatecnica.customerservice.application.dto.ClienteUpdateRequest;
import com.pruebatecnica.customerservice.application.service.ClienteService;
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
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteCreateRequest request) {
        ClienteResponse response = clienteService.create(request);
        return ResponseEntity
                .created(URI.create("/clientes/" + response.clienteId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<ClienteResponse> findByClienteId(@PathVariable String clienteId) {
        return ResponseEntity.ok(clienteService.findByClienteId(clienteId));
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<ClienteResponse> update(
            @PathVariable String clienteId,
            @Valid @RequestBody ClienteUpdateRequest request
    ) {
        return ResponseEntity.ok(clienteService.update(clienteId, request));
    }

    @PatchMapping("/{clienteId}/estado")
    public ResponseEntity<ClienteResponse> updateEstado(
            @PathVariable String clienteId,
            @Valid @RequestBody ClienteEstadoRequest request
    ) {
        return ResponseEntity.ok(clienteService.updateEstado(clienteId, request));
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> delete(@PathVariable String clienteId) {
        clienteService.delete(clienteId);
        return ResponseEntity.noContent().build();
    }
}
