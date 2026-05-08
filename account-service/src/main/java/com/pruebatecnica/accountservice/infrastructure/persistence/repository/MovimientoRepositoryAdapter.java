package com.pruebatecnica.accountservice.infrastructure.persistence.repository;

import com.pruebatecnica.accountservice.application.service.MovimientoRepositoryPort;
import com.pruebatecnica.accountservice.domain.model.Movimiento;
import com.pruebatecnica.accountservice.infrastructure.persistence.entity.CuentaEntity;
import com.pruebatecnica.accountservice.infrastructure.persistence.mapper.MovimientoMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class MovimientoRepositoryAdapter implements MovimientoRepositoryPort {

    private final MovimientoJpaRepository movimientoJpaRepository;
    private final CuentaJpaRepository cuentaJpaRepository;
    private final MovimientoMapper movimientoMapper;

    public MovimientoRepositoryAdapter(
            MovimientoJpaRepository movimientoJpaRepository,
            CuentaJpaRepository cuentaJpaRepository,
            MovimientoMapper movimientoMapper
    ) {
        this.movimientoJpaRepository = movimientoJpaRepository;
        this.cuentaJpaRepository = cuentaJpaRepository;
        this.movimientoMapper = movimientoMapper;
    }

    @Override
    public Movimiento save(Movimiento movimiento) {
        CuentaEntity cuenta = cuentaJpaRepository.getReferenceById(movimiento.getCuentaId());
        return movimientoMapper.toDomain(movimientoJpaRepository.save(movimientoMapper.toEntity(movimiento, cuenta)));
    }

    @Override
    public List<Movimiento> findAll() {
        return movimientoJpaRepository.findAll().stream().map(movimientoMapper::toDomain).toList();
    }

    @Override
    public List<Movimiento> findAllActive() {
        return movimientoJpaRepository.findByEstadoTrue().stream().map(movimientoMapper::toDomain).toList();
    }

    @Override
    public Optional<Movimiento> findById(Long id) {
        return movimientoJpaRepository.findById(id).map(movimientoMapper::toDomain);
    }

    @Override
    public List<Movimiento> findActiveByCuentaIdsAndFechaBetween(List<Long> cuentaIds, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoJpaRepository.findByCuenta_IdInAndEstadoTrueAndFechaBetweenOrderByFechaAsc(cuentaIds, fechaInicio, fechaFin)
                .stream()
                .map(movimientoMapper::toDomain)
                .toList();
    }
}
