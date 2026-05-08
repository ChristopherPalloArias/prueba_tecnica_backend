package com.pruebatecnica.accountservice.application.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MovimientoCreateRequest(
        @NotNull(message = "La cuenta es obligatoria")
        Long cuentaId,

        @NotNull(message = "El valor es obligatorio")
        BigDecimal valor
) {
}
