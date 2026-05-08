package com.pruebatecnica.accountservice.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CuentaUpdateRequest(
        @NotBlank(message = "El tipo de cuenta es obligatorio")
        String tipoCuenta
) {
}
