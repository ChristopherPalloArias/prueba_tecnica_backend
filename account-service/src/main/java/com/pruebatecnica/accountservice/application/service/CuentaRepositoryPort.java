package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.domain.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepositoryPort {

    Cuenta save(Cuenta cuenta);

    List<Cuenta> findAll();

    List<Cuenta> findAllActive();

    List<Cuenta> findByClienteId(String clienteId);

    Optional<Cuenta> findById(Long id);

    boolean existsByNumeroCuenta(String numeroCuenta);
}
