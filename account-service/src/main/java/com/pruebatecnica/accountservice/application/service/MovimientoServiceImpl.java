package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.application.dto.EstadoRequest;
import com.pruebatecnica.accountservice.application.dto.MovimientoCreateRequest;
import com.pruebatecnica.accountservice.application.dto.MovimientoResponse;
import com.pruebatecnica.accountservice.application.dto.MovimientoUpdateRequest;
import com.pruebatecnica.accountservice.domain.exception.NotFoundException;
import com.pruebatecnica.accountservice.domain.model.Cuenta;
import com.pruebatecnica.accountservice.domain.model.Movimiento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepositoryPort movimientoRepository;
    private final CuentaRepositoryPort cuentaRepository;

    public MovimientoServiceImpl(MovimientoRepositoryPort movimientoRepository, CuentaRepositoryPort cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    @Transactional
    public MovimientoResponse create(MovimientoCreateRequest request) {
        Cuenta cuenta = cuentaRepository.findById(request.cuentaId())
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        Movimiento movimiento = cuenta.registrarMovimiento(request.valor());
        cuentaRepository.save(cuenta);
        Movimiento saved = movimientoRepository.save(movimiento);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponse> findAll(boolean incluirInactivos) {
        List<Movimiento> movimientos = incluirInactivos
                ? movimientoRepository.findAll()
                : movimientoRepository.findAllActive();
        return movimientos.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoResponse findById(Long id) {
        return toResponse(getMovimiento(id));
    }

    @Override
    @Transactional
    public MovimientoResponse update(Long id, MovimientoUpdateRequest request) {
        Movimiento movimiento = getMovimiento(id);
        movimiento.actualizarDescripcion(request.descripcion());
        return toResponse(movimientoRepository.save(movimiento));
    }

    @Override
    @Transactional
    public MovimientoResponse updateEstado(Long id, EstadoRequest request) {
        Movimiento movimiento = getMovimiento(id);
        movimiento.cambiarEstado(request.estado());
        return toResponse(movimientoRepository.save(movimiento));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Movimiento movimiento = getMovimiento(id);
        movimiento.desactivar();
        movimientoRepository.save(movimiento);
    }

    private Movimiento getMovimiento(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimiento no encontrado"));
    }

    private MovimientoResponse toResponse(Movimiento movimiento) {
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo(),
                movimiento.isEstado(),
                movimiento.getCuentaId(),
                movimiento.getDescripcion()
        );
    }
}
