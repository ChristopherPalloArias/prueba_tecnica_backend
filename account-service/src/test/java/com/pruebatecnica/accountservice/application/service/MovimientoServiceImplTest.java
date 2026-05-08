package com.pruebatecnica.accountservice.application.service;

import com.pruebatecnica.accountservice.application.dto.MovimientoCreateRequest;
import com.pruebatecnica.accountservice.application.dto.MovimientoResponse;
import com.pruebatecnica.accountservice.domain.exception.BusinessException;
import com.pruebatecnica.accountservice.domain.model.Cuenta;
import com.pruebatecnica.accountservice.domain.model.Movimiento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceImplTest {

    @Mock
    private MovimientoRepositoryPort movimientoRepository;

    @Mock
    private CuentaRepositoryPort cuentaRepository;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    @Test
    void createDebeActualizarSaldoYGuardarMovimientoEnMismaOperacion() {
        Cuenta cuenta = Cuenta.reconstruir(
                1L,
                "478758",
                "Ahorro",
                new BigDecimal("1000.00"),
                new BigDecimal("1000.00"),
                true,
                "CLI-001"
        );
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(movimientoRepository.save(any(Movimiento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MovimientoResponse response = movimientoService.create(new MovimientoCreateRequest(1L, new BigDecimal("-300.00")));

        ArgumentCaptor<Cuenta> cuentaCaptor = ArgumentCaptor.forClass(Cuenta.class);
        verify(cuentaRepository).save(cuentaCaptor.capture());
        verify(movimientoRepository).save(any(Movimiento.class));
        assertEquals(new BigDecimal("700.00"), cuentaCaptor.getValue().getSaldoDisponible());
        assertEquals(new BigDecimal("700.00"), response.saldo());
        assertEquals(new BigDecimal("-300.00"), response.valor());
    }

    @Test
    void createDebeRechazarRetiroSinSaldoYNoGuardar() {
        Cuenta cuenta = Cuenta.reconstruir(
                1L,
                "478758",
                "Ahorro",
                new BigDecimal("100.00"),
                new BigDecimal("100.00"),
                true,
                "CLI-001"
        );
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> movimientoService.create(new MovimientoCreateRequest(1L, new BigDecimal("-200.00")))
        );

        assertEquals("Saldo no disponible", exception.getMessage());
        verify(cuentaRepository, never()).save(any(Cuenta.class));
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }
}
