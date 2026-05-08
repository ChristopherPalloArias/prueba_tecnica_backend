package com.pruebatecnica.customerservice.application.dto;

import jakarta.validation.constraints.NotNull;

public record ClienteEstadoRequest(
        @NotNull(message = "El estado es obligatorio")
        Boolean estado
) {
}
