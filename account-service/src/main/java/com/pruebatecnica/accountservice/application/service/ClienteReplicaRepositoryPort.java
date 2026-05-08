package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.domain.model.ClienteReplica;

import java.util.Optional;

public interface ClienteReplicaRepositoryPort {

    ClienteReplica save(ClienteReplica clienteReplica);

    Optional<ClienteReplica> findByClienteId(String clienteId);
}
