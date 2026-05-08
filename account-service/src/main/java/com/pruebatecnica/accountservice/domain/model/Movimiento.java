package com.pruebatecnica.accountservice.domain.model;

import com.pruebatecnica.accountservice.domain.exception.DomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Movimiento {

    private Long id;
    private LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private boolean estado;
    private Long cuentaId;
    private String descripcion;

    private Movimiento(
            Long id,
            LocalDateTime fecha,
            TipoMovimiento tipoMovimiento,
            BigDecimal valor,
            BigDecimal saldo,
            boolean estado,
            Long cuentaId,
            String descripcion
    ) {
        if (fecha == null) {
            throw new DomainException("La fecha del movimiento es obligatoria");
        }
        if (tipoMovimiento == null) {
            throw new DomainException("El tipo de movimiento es obligatorio");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) == 0) {
            throw new DomainException("El valor del movimiento no puede ser cero");
        }
        if (saldo == null) {
            throw new DomainException("El saldo del movimiento es obligatorio");
        }
        if (cuentaId == null) {
            throw new DomainException("La cuenta del movimiento es obligatoria");
        }
        this.id = id;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.estado = estado;
        this.cuentaId = cuentaId;
        this.descripcion = normalizar(descripcion);
    }

    public static Movimiento crear(Long cuentaId, TipoMovimiento tipoMovimiento, BigDecimal valor, BigDecimal saldo) {
        return new Movimiento(null, LocalDateTime.now(), tipoMovimiento, valor, saldo, true, cuentaId, null);
    }

    public static Movimiento reconstruir(
            Long id,
            LocalDateTime fecha,
            TipoMovimiento tipoMovimiento,
            BigDecimal valor,
            BigDecimal saldo,
            boolean estado,
            Long cuentaId,
            String descripcion
    ) {
        return new Movimiento(id, fecha, tipoMovimiento, valor, saldo, estado, cuentaId, descripcion);
    }

    public void actualizarDescripcion(String descripcion) {
        this.descripcion = normalizar(descripcion);
    }

    public void cambiarEstado(boolean estado) {
        this.estado = estado;
    }

    public void desactivar() {
        this.estado = false;
    }

    private static String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public boolean isEstado() {
        return estado;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
