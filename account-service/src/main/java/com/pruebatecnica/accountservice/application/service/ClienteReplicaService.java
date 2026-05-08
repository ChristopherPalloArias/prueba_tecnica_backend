package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.domain.model.ClienteReplica;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteReplicaService {

    private final ClienteReplicaRepositoryPort clienteReplicaRepository;

    public ClienteReplicaService(ClienteReplicaRepositoryPort clienteReplicaRepository) {
        this.clienteReplicaRepository = clienteReplicaRepository;
    }

    @Transactional
    public void upsert(String clienteId, String nombre, String identificacion, boolean estado) {
        ClienteReplica clienteReplica = clienteReplicaRepository.findByClienteId(clienteId)
                .map(existing -> {
                    existing.actualizar(nombre, identificacion, estado);
                    return existing;
                })
                .orElseGet(() -> ClienteReplica.crear(clienteId, nombre, identificacion, estado));
        clienteReplicaRepository.save(clienteReplica);
    }
}
