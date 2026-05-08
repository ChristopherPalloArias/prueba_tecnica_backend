package com.pruebatecnica.accountservice.infrastructure.persistence.mapper;

import com.pruebatecnica.accountservice.domain.model.Cuenta;
import com.pruebatecnica.accountservice.infrastructure.persistence.entity.CuentaEntity;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {

    public Cuenta toDomain(CuentaEntity entity) {
        return Cuenta.reconstruir(
                entity.getId(),
                entity.getNumeroCuenta(),
                entity.getTipoCuenta(),
                entity.getSaldoInicial(),
                entity.getSaldoDisponible(),
                Boolean.TRUE.equals(entity.getEstado()),
                entity.getClienteId(),
                entity.getVersion()
        );
    }

    public CuentaEntity toEntity(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity();
        entity.setId(cuenta.getId());
        entity.setNumeroCuenta(cuenta.getNumeroCuenta());
        entity.setTipoCuenta(cuenta.getTipoCuenta());
        entity.setSaldoInicial(cuenta.getSaldoInicial());
        entity.setSaldoDisponible(cuenta.getSaldoDisponible());
        entity.setEstado(cuenta.isEstado());
        entity.setClienteId(cuenta.getClienteId());
        entity.setVersion(cuenta.getVersion());
        return entity;
    }
}
