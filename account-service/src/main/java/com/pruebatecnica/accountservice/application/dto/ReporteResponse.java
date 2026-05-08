package com.pruebatecnica.accountservice.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReporteResponse(
        LocalDate fecha,
        String cliente,
        String numeroCuenta,
        String tipo,
        BigDecimal saldoInicial,
        Boolean estado,
        BigDecimal movimiento,
        BigDecimal saldoDisponible
) {
}
