package com.pruebatecnica.accountservice.domain.model;

import com.pruebatecnica.accountservice.domain.exception.BusinessException;
import com.pruebatecnica.accountservice.domain.exception.DomainException;

import java.math.BigDecimal;

public class Cuenta {

    private Long id;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoDisponible;
    private boolean estado;
    private String clienteId;
    private Long version;

    private Cuenta(
            Long id,
            String numeroCuenta,
            String tipoCuenta,
            BigDecimal saldoInicial,
            BigDecimal saldoDisponible,
            boolean estado,
            String clienteId,
            Long version
    ) {
        validarObligatorio(numeroCuenta, "El número de cuenta es obligatorio");
        validarObligatorio(tipoCuenta, "El tipo de cuenta es obligatorio");
        validarObligatorio(clienteId, "El clienteId es obligatorio");
        validarSaldoInicial(saldoInicial);
        if (saldoDisponible == null) {
            throw new DomainException("El saldo disponible es obligatorio");
        }
        this.id = id;
        this.numeroCuenta = numeroCuenta.trim();
        this.tipoCuenta = tipoCuenta.trim();
        this.saldoInicial = saldoInicial;
        this.saldoDisponible = saldoDisponible;
        this.estado = estado;
        this.clienteId = clienteId.trim();
        this.version = version;
    }

    public static Cuenta crear(String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, String clienteId) {
        return new Cuenta(null, numeroCuenta, tipoCuenta, saldoInicial, saldoInicial, true, clienteId, null);
    }

    public static Cuenta reconstruir(
            Long id,
            String numeroCuenta,
            String tipoCuenta,
            BigDecimal saldoInicial,
            BigDecimal saldoDisponible,
            boolean estado,
            String clienteId,
            Long version
    ) {
        return new Cuenta(id, numeroCuenta, tipoCuenta, saldoInicial, saldoDisponible, estado, clienteId, version);
    }

    public void actualizarDatos(String tipoCuenta) {
        validarObligatorio(tipoCuenta, "El tipo de cuenta es obligatorio");
        this.tipoCuenta = tipoCuenta.trim();
    }

    public Movimiento registrarMovimiento(BigDecimal valor) {
        if (!estado) {
            throw new BusinessException("Cuenta inactiva");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) == 0) {
            throw new DomainException("El valor del movimiento no puede ser cero");
        }

        BigDecimal nuevoSaldo = saldoDisponible.add(valor);
        if (valor.compareTo(BigDecimal.ZERO) < 0 && nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Saldo no disponible");
        }

        this.saldoDisponible = nuevoSaldo;
        TipoMovimiento tipoMovimiento = valor.compareTo(BigDecimal.ZERO) > 0
                ? TipoMovimiento.DEPOSITO
                : TipoMovimiento.RETIRO;
        return Movimiento.crear(id, tipoMovimiento, valor, nuevoSaldo);
    }

    public void cambiarEstado(boolean estado) {
        this.estado = estado;
    }

    public void desactivar() {
        this.estado = false;
    }

    private static void validarSaldoInicial(BigDecimal saldoInicial) {
        if (saldoInicial == null) {
            throw new DomainException("El saldo inicial es obligatorio");
        }
        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("El saldo inicial no puede ser negativo");
        }
    }

    private static void validarObligatorio(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DomainException(mensaje);
        }
    }

    public Long getId() {
        return id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public boolean isEstado() {
        return estado;
    }

    public String getClienteId() {
        return clienteId;
    }

    public Long getVersion() {
        return version;
    }
}
