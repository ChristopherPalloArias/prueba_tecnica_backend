package com.pruebatecnica.accountservice.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebatecnica.accountservice.application.dto.MovimientoCreateRequest;
import com.pruebatecnica.accountservice.application.dto.MovimientoResponse;
import com.pruebatecnica.accountservice.application.service.MovimientoService;
import com.pruebatecnica.accountservice.domain.exception.BusinessException;
import com.pruebatecnica.accountservice.domain.model.TipoMovimiento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovimientoController.class)
class MovimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovimientoService movimientoService;

    @Test
    void createDebeRegistrarMovimientoExitoso() throws Exception {
        MovimientoCreateRequest request = new MovimientoCreateRequest(1L, new BigDecimal("150.00"));
        MovimientoResponse response = new MovimientoResponse(
                10L,
                LocalDateTime.of(2026, 5, 8, 10, 30),
                TipoMovimiento.DEPOSITO,
                new BigDecimal("150.00"),
                new BigDecimal("1150.00"),
                true,
                1L,
                null
        );

        when(movimientoService.create(any(MovimientoCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/movimientos/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.tipoMovimiento").value("DEPOSITO"))
                .andExpect(jsonPath("$.valor").value(150.00))
                .andExpect(jsonPath("$.saldo").value(1150.00));
    }

    @Test
    void createDebeRetornarSaldoNoDisponibleConMensajeExacto() throws Exception {
        MovimientoCreateRequest request = new MovimientoCreateRequest(1L, new BigDecimal("-2000.00"));

        when(movimientoService.create(any(MovimientoCreateRequest.class)))
                .thenThrow(new BusinessException("Saldo no disponible"));

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }
}
