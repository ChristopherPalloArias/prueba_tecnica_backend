package com.pruebatecnica.customerservice.domain.model;

import com.pruebatecnica.customerservice.domain.exception.DomainException;

public class Persona {

    private Long id;
    private String nombre;
    private String genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;

    protected Persona(
            Long id,
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono
    ) {
        validarTextoObligatorio(nombre, "El nombre es obligatorio");
        validarTextoObligatorio(identificacion, "La identificación es obligatoria");
        validarEdad(edad);
        this.id = id;
        this.nombre = nombre.trim();
        this.genero = normalizar(genero);
        this.edad = edad;
        this.identificacion = identificacion.trim();
        this.direccion = normalizar(direccion);
        this.telefono = normalizar(telefono);
    }

    protected void actualizarDatosPersona(
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono
    ) {
        validarTextoObligatorio(nombre, "El nombre es obligatorio");
        validarTextoObligatorio(identificacion, "La identificación es obligatoria");
        validarEdad(edad);
        this.nombre = nombre.trim();
        this.genero = normalizar(genero);
        this.edad = edad;
        this.identificacion = identificacion.trim();
        this.direccion = normalizar(direccion);
        this.telefono = normalizar(telefono);
    }

    protected static void validarTextoObligatorio(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DomainException(mensaje);
        }
    }

    private static void validarEdad(Integer edad) {
        if (edad != null && edad < 0) {
            throw new DomainException("La edad no puede ser negativa");
        }
    }

    private static String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
}
