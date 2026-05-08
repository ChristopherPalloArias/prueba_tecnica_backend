package com.pruebatecnica.customerservice.domain.model;

public class Cliente extends Persona {

    private final String clienteId;
    private String contrasena;
    private boolean estado;

    private Cliente(
            Long id,
            String clienteId,
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String contrasena,
            boolean estado
    ) {
        super(id, nombre, genero, edad, identificacion, direccion, telefono);
        validarTextoObligatorio(clienteId, "El clienteId es obligatorio");
        validarTextoObligatorio(contrasena, "La contraseña es obligatoria");
        this.clienteId = clienteId.trim();
        this.contrasena = contrasena.trim();
        this.estado = estado;
    }

    public static Cliente crear(
            String clienteId,
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String contrasena
    ) {
        return new Cliente(null, clienteId, nombre, genero, edad, identificacion, direccion, telefono, contrasena, true);
    }

    public static Cliente reconstruir(
            Long id,
            String clienteId,
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String contrasena,
            boolean estado
    ) {
        return new Cliente(id, clienteId, nombre, genero, edad, identificacion, direccion, telefono, contrasena, estado);
    }

    public void actualizarDatos(
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String contrasena
    ) {
        actualizarDatosPersona(nombre, genero, edad, identificacion, direccion, telefono);
        validarTextoObligatorio(contrasena, "La contraseña es obligatoria");
        this.contrasena = contrasena.trim();
    }

    public void cambiarEstado(boolean estado) {
        this.estado = estado;
    }

    public void desactivar() {
        this.estado = false;
    }

    public String getClienteId() {
        return clienteId;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean isEstado() {
        return estado;
    }
}
