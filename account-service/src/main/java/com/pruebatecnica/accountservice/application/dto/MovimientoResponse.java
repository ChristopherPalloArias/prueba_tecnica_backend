package com.pruebatecnica.accountservice.application.dto;

import com.pruebatecnica.accountservice.domain.model.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoResponse(
        Long id,
        LocalDateTime fecha,
        TipoMovimiento tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo,
        Boolean estado,
        Long cuentaId,
        String descripcion
) {
}
