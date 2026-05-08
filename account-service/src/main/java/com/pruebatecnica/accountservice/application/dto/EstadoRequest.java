package com.pruebatecnica.accountservice.application.dto;

import jakarta.validation.constraints.NotNull;

public record EstadoRequest(
        @NotNull(message = "El estado es obligatorio")
        Boolean estado
) {
}
