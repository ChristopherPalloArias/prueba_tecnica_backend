package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.application.dto.CuentaCreateRequest;
import com.pruebatecnica.accountservice.application.dto.CuentaResponse;
import com.pruebatecnica.accountservice.application.dto.CuentaUpdateRequest;
import com.pruebatecnica.accountservice.application.dto.EstadoRequest;
import com.pruebatecnica.accountservice.domain.exception.BusinessException;
import com.pruebatecnica.accountservice.domain.exception.NotFoundException;
import com.pruebatecnica.accountservice.domain.model.ClienteReplica;
import com.pruebatecnica.accountservice.domain.model.Cuenta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepositoryPort cuentaRepository;
    private final ClienteReplicaRepositoryPort clienteReplicaRepository;

    public CuentaServiceImpl(CuentaRepositoryPort cuentaRepository, ClienteReplicaRepositoryPort clienteReplicaRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteReplicaRepository = clienteReplicaRepository;
    }

    @Override
    @Transactional
    public CuentaResponse create(CuentaCreateRequest request) {
        if (cuentaRepository.existsByNumeroCuenta(request.numeroCuenta())) {
            throw new BusinessException("El número de cuenta ya existe");
        }

        ClienteReplica cliente = clienteReplicaRepository.findByClienteId(request.clienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        if (!cliente.isEstado()) {
            throw new BusinessException("Cliente inactivo");
        }

        Cuenta cuenta = Cuenta.crear(request.numeroCuenta(), request.tipoCuenta(), request.saldoInicial(), request.clienteId());
        return toResponse(cuentaRepository.save(cuenta));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponse> findAll(boolean incluirInactivas) {
        List<Cuenta> cuentas = incluirInactivas ? cuentaRepository.findAll() : cuentaRepository.findAllActive();
        return cuentas.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponse findById(Long id) {
        return toResponse(getCuenta(id));
    }

    @Override
    @Transactional
    public CuentaResponse update(Long id, CuentaUpdateRequest request) {
        Cuenta cuenta = getCuenta(id);
        cuenta.actualizarDatos(request.tipoCuenta());
        return toResponse(cuentaRepository.save(cuenta));
    }

    @Override
    @Transactional
    public CuentaResponse updateEstado(Long id, EstadoRequest request) {
        Cuenta cuenta = getCuenta(id);
        cuenta.cambiarEstado(request.estado());
        return toResponse(cuentaRepository.save(cuenta));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Cuenta cuenta = getCuenta(id);
        cuenta.desactivar();
        cuentaRepository.save(cuenta);
    }

    private Cuenta getCuenta(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
    }

    private CuentaResponse toResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.isEstado(),
                cuenta.getClienteId()
        );
    }
}
