package com.pruebatecnica.accountservice.infrastructure.persistence.repository;

import com.pruebatecnica.accountservice.domain.model.TipoMovimiento;
import com.pruebatecnica.accountservice.infrastructure.persistence.entity.CuentaEntity;
import com.pruebatecnica.accountservice.infrastructure.persistence.entity.MovimientoEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

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

    @Autowired
    private EntityManager entityManager;

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
        assertEquals(1, movimientoJpaRepository.findByCuenta_IdInAndEstadoTrueAndFechaBetweenOrderByFechaAsc(
                java.util.List.of(cuenta.getId()),
                LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 5, 31, 23, 59)
        ).size());
    }

    @Test
    void cuentaDebeRechazarSaldosNegativosPorConstraint() {
        CuentaEntity cuenta = cuenta("478758");
        cuenta.setSaldoDisponible(new BigDecimal("-1.00"));

        assertThrows(DataIntegrityViolationException.class, () ->
                cuentaJpaRepository.saveAndFlush(cuenta)
        );
    }

    @Test
    void movimientoDebeRechazarValorCeroPorConstraint() {
        CuentaEntity cuenta = cuentaJpaRepository.saveAndFlush(cuenta("478758"));

        assertThrows(DataIntegrityViolationException.class, () ->
                movimientoJpaRepository.saveAndFlush(movimiento(cuenta, BigDecimal.ZERO))
        );
    }

    @Test
    void cuentaDebeUsarVersionParaRechazarActualizacionConcurrenteObsoleta() {
        CuentaEntity cuenta = cuentaJpaRepository.saveAndFlush(cuenta("478758"));
        Long cuentaId = cuenta.getId();
        Long versionOriginal = cuenta.getVersion();
        entityManager.detach(cuenta);

        CuentaEntity actualizada = cuentaJpaRepository.findById(cuentaId).orElseThrow();
        actualizada.setSaldoDisponible(new BigDecimal("900.00"));
        cuentaJpaRepository.saveAndFlush(actualizada);
        entityManager.clear();

        CuentaEntity obsoleta = cuenta("478758");
        obsoleta.setId(cuentaId);
        obsoleta.setVersion(versionOriginal);
        obsoleta.setSaldoDisponible(new BigDecimal("800.00"));

        assertThrows(OptimisticLockingFailureException.class, () ->
                cuentaJpaRepository.saveAndFlush(obsoleta)
        );
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

    private MovimientoEntity movimiento(CuentaEntity cuenta, BigDecimal valor) {
        MovimientoEntity movimiento = new MovimientoEntity();
        movimiento.setFecha(LocalDateTime.of(2026, 5, 8, 10, 0));
        movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        movimiento.setValor(valor);
        movimiento.setSaldo(new BigDecimal("1000.00"));
        movimiento.setEstado(true);
        movimiento.setCuenta(cuenta);
        return movimiento;
    }
}
