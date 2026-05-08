package com.pruebatecnica.accountservice.infrastructure.persistence.repository;

import com.pruebatecnica.accountservice.application.service.CuentaRepositoryPort;
import com.pruebatecnica.accountservice.domain.model.Cuenta;
import com.pruebatecnica.accountservice.infrastructure.persistence.mapper.CuentaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CuentaRepositoryAdapter implements CuentaRepositoryPort {

    private final CuentaJpaRepository cuentaJpaRepository;
    private final CuentaMapper cuentaMapper;

    public CuentaRepositoryAdapter(CuentaJpaRepository cuentaJpaRepository, CuentaMapper cuentaMapper) {
        this.cuentaJpaRepository = cuentaJpaRepository;
        this.cuentaMapper = cuentaMapper;
    }

    @Override
    public Cuenta save(Cuenta cuenta) {
        return cuentaMapper.toDomain(cuentaJpaRepository.save(cuentaMapper.toEntity(cuenta)));
    }

    @Override
    public List<Cuenta> findAll() {
        return cuentaJpaRepository.findAll().stream().map(cuentaMapper::toDomain).toList();
    }

    @Override
    public List<Cuenta> findAllActive() {
        return cuentaJpaRepository.findByEstadoTrue().stream().map(cuentaMapper::toDomain).toList();
    }

    @Override
    public List<Cuenta> findByClienteId(String clienteId) {
        return cuentaJpaRepository.findByClienteId(clienteId).stream().map(cuentaMapper::toDomain).toList();
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return cuentaJpaRepository.findById(id).map(cuentaMapper::toDomain);
    }

    @Override
    public boolean existsByNumeroCuenta(String numeroCuenta) {
        return cuentaJpaRepository.existsByNumeroCuenta(numeroCuenta);
    }
}
