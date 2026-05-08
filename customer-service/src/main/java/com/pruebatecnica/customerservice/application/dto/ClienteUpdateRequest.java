package com.pruebatecnica.customerservice.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteUpdateRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120, message = "El nombre no debe superar 120 caracteres")
        String nombre,

        @Size(max = 20, message = "El género no debe superar 20 caracteres")
        String genero,

        @Min(value = 0, message = "La edad no puede ser negativa")
        Integer edad,

        @NotBlank(message = "La identificación es obligatoria")
        @Size(max = 30, message = "La identificación no debe superar 30 caracteres")
        String identificacion,

        @Size(max = 200, message = "La dirección no debe superar 200 caracteres")
        String direccion,

        @Size(max = 30, message = "El teléfono no debe superar 30 caracteres")
        String telefono,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(max = 120, message = "La contraseña no debe superar 120 caracteres")
        String contrasena
) {
}
