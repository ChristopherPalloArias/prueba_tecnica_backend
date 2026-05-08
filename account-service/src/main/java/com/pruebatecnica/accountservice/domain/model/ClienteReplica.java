package com.pruebatecnica.accountservice.domain.model;

import com.pruebatecnica.accountservice.domain.exception.DomainException;

public class ClienteReplica {

    private final String clienteId;
    private String nombre;
    private String identificacion;
    private boolean estado;

    private ClienteReplica(String clienteId, String nombre, String identificacion, boolean estado) {
        validarObligatorio(clienteId, "El clienteId es obligatorio");
        validarObligatorio(nombre, "El nombre del cliente es obligatorio");
        validarObligatorio(identificacion, "La identificación del cliente es obligatoria");
        this.clienteId = clienteId.trim();
        this.nombre = nombre.trim();
        this.identificacion = identificacion.trim();
        this.estado = estado;
    }

    public static ClienteReplica crear(String clienteId, String nombre, String identificacion, boolean estado) {
        return new ClienteReplica(clienteId, nombre, identificacion, estado);
    }

    public void actualizar(String nombre, String identificacion, boolean estado) {
        validarObligatorio(nombre, "El nombre del cliente es obligatorio");
        validarObligatorio(identificacion, "La identificación del cliente es obligatoria");
        this.nombre = nombre.trim();
        this.identificacion = identificacion.trim();
        this.estado = estado;
    }

    private static void validarObligatorio(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DomainException(mensaje);
        }
    }

    public String getClienteId() {
        return clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public boolean isEstado() {
        return estado;
    }
}
