package com.pruebatecnica.accountservice.infrastructure.persistence.repository;

import com.pruebatecnica.accountservice.domain.model.TipoMovimiento;
import com.pruebatecnica.accountservice.infrastructure.persistence.entity.CuentaEntity;
import com.pruebatecnica.accountservice.infrastructure.persistence.entity.MovimientoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AccountPersistenceTest {

    @Autowired
    private CuentaJpaRepository cuentaJpaRepository;

    @Autowired
    private MovimientoJpaRepository movimientoJpaRepository;

    @Test
    void cuentaDebeRechazarNumeroCuentaDuplicado() {
        cuentaJpaRepository.saveAndFlush(cuenta("478758"));

        assertThrows(DataIntegrityViolationException.class, () ->
                cuentaJpaRepository.saveAndFlush(cuenta("478758"))
        );
    }

    @Test
    void movimientoDebePersistirRelacionadoACuenta() {
        CuentaEntity cuenta = cuentaJpaRepository.saveAndFlush(cuenta("478758"));

        MovimientoEntity movimiento = new MovimientoEntity();
        movimiento.setFecha(LocalDateTime.of(2026, 5, 8, 10, 0));
        movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        movimiento.setValor(new BigDecimal("100.00"));
        movimiento.setSaldo(new BigDecimal("1100.00"));
        movimiento.setEstado(true);
        movimiento.setCuenta(cuenta);

        MovimientoEntity saved = movimientoJpaRepository.saveAndFlush(movimiento);

        assertTrue(movimientoJpaRepository.findById(saved.getId()).isPresent());
        assertEquals(1, movimientoJpaRepository.findByCuenta_IdInAndFechaBetweenOrderByFechaAsc(
                java.util.List.of(cuenta.getId()),
                LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 5, 31, 23, 59)
        ).size());
    }

    private CuentaEntity cuenta(String numeroCuenta) {
        CuentaEntity cuenta = new CuentaEntity();
        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setTipoCuenta("Ahorro");
        cuenta.setSaldoInicial(new BigDecimal("1000.00"));
        cuenta.setSaldoDisponible(new BigDecimal("1000.00"));
        cuenta.setEstado(true);
        cuenta.setClienteId("CLI-001");
        return cuenta;
    }
}
