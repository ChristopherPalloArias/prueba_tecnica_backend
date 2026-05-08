package com.pruebatecnica.accountservice.application.dto;

import jakarta.validation.constraints.Size;

public record MovimientoUpdateRequest(
        @Size(max = 200, message = "La descripción no debe superar 200 caracteres")
        String descripcion
) {
}
