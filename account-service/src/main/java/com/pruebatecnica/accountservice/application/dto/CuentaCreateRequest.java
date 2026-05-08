package com.pruebatecnica.accountservice.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CuentaCreateRequest(
        @NotBlank(message = "El número de cuenta es obligatorio")
        String numeroCuenta,

        @NotBlank(message = "El tipo de cuenta es obligatorio")
        String tipoCuenta,

        @NotNull(message = "El saldo inicial es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial no puede ser negativo")
        BigDecimal saldoInicial,

        @NotBlank(message = "El clienteId es obligatorio")
        String clienteId
) {
}
