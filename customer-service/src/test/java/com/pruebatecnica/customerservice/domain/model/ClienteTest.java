package com.pruebatecnica.customerservice.domain.model;

import com.pruebatecnica.customerservice.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClienteTest {

    @Test
    void crearDebeInicializarClienteActivo() {
        Cliente cliente = Cliente.crear(
                "CLI-001",
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "0987654321",
                "1234"
        );

        assertTrue(cliente.isEstado());
    }

    @Test
    void desactivarDebeCambiarEstadoAFalso() {
        Cliente cliente = Cliente.crear(
                "CLI-001",
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "0987654321",
                "1234"
        );

        cliente.desactivar();

        assertFalse(cliente.isEstado());
    }

    @Test
    void crearNoDebePermitirClienteIdVacio() {
        assertThrows(DomainException.class, () -> Cliente.crear(
                " ",
                "Jose Lema",
                "Masculino",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "0987654321",
                "1234"
        ));
    }
}
