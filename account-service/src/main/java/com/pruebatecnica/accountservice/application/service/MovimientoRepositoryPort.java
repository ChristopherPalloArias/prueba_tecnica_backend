package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.domain.model.Movimiento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepositoryPort {

    Movimiento save(Movimiento movimiento);

    List<Movimiento> findAll();

    List<Movimiento> findAllActive();

    Optional<Movimiento> findById(Long id);

    List<Movimiento> findByCuentaIdsAndFechaBetween(List<Long> cuentaIds, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
