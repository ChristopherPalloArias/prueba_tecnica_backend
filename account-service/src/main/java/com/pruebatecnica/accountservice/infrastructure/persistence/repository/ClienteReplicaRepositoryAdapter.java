package com.pruebatecnica.accountservice.infrastructure.persistence.repository;

import com.pruebatecnica.accountservice.application.service.ClienteReplicaRepositoryPort;
import com.pruebatecnica.accountservice.domain.model.ClienteReplica;
import com.pruebatecnica.accountservice.infrastructure.persistence.mapper.ClienteReplicaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClienteReplicaRepositoryAdapter implements ClienteReplicaRepositoryPort {

    private final ClienteReplicaJpaRepository clienteReplicaJpaRepository;
    private final ClienteReplicaMapper clienteReplicaMapper;

    public ClienteReplicaRepositoryAdapter(
            ClienteReplicaJpaRepository clienteReplicaJpaRepository,
            ClienteReplicaMapper clienteReplicaMapper
    ) {
        this.clienteReplicaJpaRepository = clienteReplicaJpaRepository;
        this.clienteReplicaMapper = clienteReplicaMapper;
    }

    @Override
    public ClienteReplica save(ClienteReplica clienteReplica) {
        return clienteReplicaMapper.toDomain(clienteReplicaJpaRepository.save(clienteReplicaMapper.toEntity(clienteReplica)));
    }

    @Override
    public Optional<ClienteReplica> findByClienteId(String clienteId) {
        return clienteReplicaJpaRepository.findById(clienteId).map(clienteReplicaMapper::toDomain);
    }
}
