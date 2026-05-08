package com.pruebatecnica.customerservice.infrastructure.persistence.mapper;

import com.pruebatecnica.customerservice.domain.model.Cliente;
import com.pruebatecnica.customerservice.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toDomain(ClienteEntity entity) {
        return Cliente.reconstruir(
                entity.getId(),
                entity.getClienteId(),
                entity.getNombre(),
                entity.getGenero(),
                entity.getEdad(),
                entity.getIdentificacion(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getContrasena(),
                Boolean.TRUE.equals(entity.getEstado())
        );
    }

    public ClienteEntity toEntity(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setClienteId(cliente.getClienteId());
        entity.setNombre(cliente.getNombre());
        entity.setGenero(cliente.getGenero());
        entity.setEdad(cliente.getEdad());
        entity.setIdentificacion(cliente.getIdentificacion());
        entity.setDireccion(cliente.getDireccion());
        entity.setTelefono(cliente.getTelefono());
        entity.setContrasena(cliente.getContrasena());
        entity.setEstado(cliente.isEstado());
        return entity;
    }

}
