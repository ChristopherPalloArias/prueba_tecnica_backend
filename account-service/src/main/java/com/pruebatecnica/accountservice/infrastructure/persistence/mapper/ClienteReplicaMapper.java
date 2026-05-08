package com.pruebatecnica.accountservice.infrastructure.persistence.mapper;

import com.pruebatecnica.accountservice.domain.model.ClienteReplica;
import com.pruebatecnica.accountservice.infrastructure.persistence.entity.ClienteReplicaEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteReplicaMapper {

    public ClienteReplica toDomain(ClienteReplicaEntity entity) {
        return ClienteReplica.crear(
                entity.getClienteId(),
                entity.getNombre(),
                entity.getIdentificacion(),
                Boolean.TRUE.equals(entity.getEstado())
        );
    }

    public ClienteReplicaEntity toEntity(ClienteReplica clienteReplica) {
        ClienteReplicaEntity entity = new ClienteReplicaEntity();
        entity.setClienteId(clienteReplica.getClienteId());
        entity.setNombre(clienteReplica.getNombre());
        entity.setIdentificacion(clienteReplica.getIdentificacion());
        entity.setEstado(clienteReplica.isEstado());
        return entity;
    }
}
