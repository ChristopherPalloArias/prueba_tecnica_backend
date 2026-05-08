package com.pruebatecnica.accountservice.infrastructure.persistence.mapper;

import com.pruebatecnica.accountservice.domain.model.Movimiento;
import com.pruebatecnica.accountservice.infrastructure.persistence.entity.CuentaEntity;
import com.pruebatecnica.accountservice.infrastructure.persistence.entity.MovimientoEntity;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {

    public Movimiento toDomain(MovimientoEntity entity) {
        return Movimiento.reconstruir(
                entity.getId(),
                entity.getFecha(),
                entity.getTipoMovimiento(),
                entity.getValor(),
                entity.getSaldo(),
                Boolean.TRUE.equals(entity.getEstado()),
                entity.getCuenta().getId(),
                entity.getDescripcion()
        );
    }

    public MovimientoEntity toEntity(Movimiento movimiento, CuentaEntity cuenta) {
        MovimientoEntity entity = new MovimientoEntity();
        entity.setId(movimiento.getId());
        entity.setFecha(movimiento.getFecha());
        entity.setTipoMovimiento(movimiento.getTipoMovimiento());
        entity.setValor(movimiento.getValor());
        entity.setSaldo(movimiento.getSaldo());
        entity.setEstado(movimiento.isEstado());
        entity.setCuenta(cuenta);
        entity.setDescripcion(movimiento.getDescripcion());
        return entity;
    }
}
