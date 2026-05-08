package com.pruebatecnica.accountservice.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes_replica")
public class ClienteReplicaEntity {

    @Id
    @Column(name = "cliente_id", length = 50)
    private String clienteId;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "identificacion", nullable = false, length = 30)
    private String identificacion;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    public ClienteReplicaEntity() {
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
