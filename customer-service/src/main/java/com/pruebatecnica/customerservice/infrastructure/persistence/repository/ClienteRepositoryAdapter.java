package com.pruebatecnica.customerservice.infrastructure.persistence.repository;

import com.pruebatecnica.customerservice.application.service.ClienteRepositoryPort;
import com.pruebatecnica.customerservice.domain.model.Cliente;
import com.pruebatecnica.customerservice.infrastructure.persistence.mapper.ClienteMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository clienteJpaRepository;
    private final ClienteMapper clienteMapper;

    public ClienteRepositoryAdapter(ClienteJpaRepository clienteJpaRepository, ClienteMapper clienteMapper) {
        this.clienteJpaRepository = clienteJpaRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteMapper.toDomain(clienteJpaRepository.save(clienteMapper.toEntity(cliente)));
    }

    @Override
    public List<Cliente> findAll() {
        return clienteJpaRepository.findAll()
                .stream()
                .map(clienteMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Cliente> findByClienteId(String clienteId) {
        return clienteJpaRepository.findByClienteId(clienteId)
                .map(clienteMapper::toDomain);
    }

    @Override
    public boolean existsByClienteId(String clienteId) {
        return clienteJpaRepository.existsByClienteId(clienteId);
    }

    @Override
    public boolean existsByIdentificacion(String identificacion) {
        return clienteJpaRepository.existsByIdentificacion(identificacion);
    }

    @Override
    public boolean existsByIdentificacionAndClienteIdNot(String identificacion, String clienteId) {
        return clienteJpaRepository.existsByIdentificacionAndClienteIdNot(identificacion, clienteId);
    }
}
