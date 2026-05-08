package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.application.dto.ReporteResponse;
import com.pruebatecnica.accountservice.domain.exception.DomainException;
import com.pruebatecnica.accountservice.domain.exception.NotFoundException;
import com.pruebatecnica.accountservice.domain.model.Cuenta;
import com.pruebatecnica.accountservice.domain.model.Movimiento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ClienteReplicaRepositoryPort clienteReplicaRepository;
    private final CuentaRepositoryPort cuentaRepository;
    private final MovimientoRepositoryPort movimientoRepository;

    public ReporteServiceImpl(
            ClienteReplicaRepositoryPort clienteReplicaRepository,
            CuentaRepositoryPort cuentaRepository,
            MovimientoRepositoryPort movimientoRepository
    ) {
        this.clienteReplicaRepository = clienteReplicaRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> generate(String clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new DomainException("El clienteId es obligatorio");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new DomainException("Las fechas son obligatorias");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new DomainException("El rango de fechas es inválido");
        }

        String nombreCliente = clienteReplicaRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"))
                .getNombre();

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        List<Long> cuentaIds = cuentas.stream().map(Cuenta::getId).toList();
        if (cuentaIds.isEmpty()) {
            return List.of();
        }

        Map<Long, Cuenta> cuentasPorId = cuentas.stream()
                .collect(Collectors.toMap(Cuenta::getId, cuenta -> cuenta));

        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdsAndFechaBetween(
                cuentaIds,
                fechaInicio.atStartOfDay(),
                fechaFin.atTime(LocalTime.MAX)
        );

        return movimientos.stream()
                .map(movimiento -> {
                    Cuenta cuenta = cuentasPorId.get(movimiento.getCuentaId());
                    return new ReporteResponse(
                            movimiento.getFecha().toLocalDate(),
                            nombreCliente,
                            cuenta.getNumeroCuenta(),
                            cuenta.getTipoCuenta(),
                            cuenta.getSaldoInicial(),
                            cuenta.isEstado(),
                            movimiento.getValor(),
                            movimiento.getSaldo()
                    );
                })
                .toList();
    }
}
