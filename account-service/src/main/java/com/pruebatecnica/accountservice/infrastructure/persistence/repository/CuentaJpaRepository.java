package com.pruebatecnica.accountservice.infrastructure.persistence.repository;

import com.pruebatecnica.accountservice.infrastructure.persistence.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaJpaRepository extends JpaRepository<CuentaEntity, Long> {

    boolean existsByNumeroCuenta(String numeroCuenta);

    List<CuentaEntity> findByEstadoTrue();

    List<CuentaEntity> findByClienteId(String clienteId);
}
