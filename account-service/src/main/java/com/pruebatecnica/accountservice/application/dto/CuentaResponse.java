package com.pruebatecnica.accountservice.application.dto;

import java.math.BigDecimal;

public record CuentaResponse(
        Long id,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        Boolean estado,
        String clienteId
) {
}
