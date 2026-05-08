package com.pruebatecnica.accountservice.infrastructure.persistence.repository;

import com.pruebatecnica.accountservice.infrastructure.persistence.entity.ClienteReplicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteReplicaJpaRepository extends JpaRepository<ClienteReplicaEntity, String> {
}
