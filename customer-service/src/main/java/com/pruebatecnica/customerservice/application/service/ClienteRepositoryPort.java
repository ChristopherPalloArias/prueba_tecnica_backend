package com.pruebatecnica.customerservice.application.service;

import com.pruebatecnica.customerservice.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {

    Cliente save(Cliente cliente);

    List<Cliente> findAll();

    List<Cliente> findAllActive();

    Optional<Cliente> findByClienteId(String clienteId);

    boolean existsByClienteId(String clienteId);

    boolean existsByIdentificacion(String identificacion);

    boolean existsByIdentificacionAndClienteIdNot(String identificacion, String clienteId);
}
