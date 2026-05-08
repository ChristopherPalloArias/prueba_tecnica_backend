package com.pruebatecnica.accountservice.infrastructure.persistence.repository;

import com.pruebatecnica.accountservice.infrastructure.persistence.entity.MovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoJpaRepository extends JpaRepository<MovimientoEntity, Long> {

    List<MovimientoEntity> findByEstadoTrue();

    List<MovimientoEntity> findByCuenta_IdInAndFechaBetweenOrderByFechaAsc(
            List<Long> cuentaIds,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );
}
