package com.pruebatecnica.customerservice.infrastructure.persistence.repository;

import com.pruebatecnica.customerservice.infrastructure.persistence.entity.ClienteEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ClienteJpaRepositoryTest {

    @Autowired
    private ClienteJpaRepository clienteJpaRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void saveDebePersistirPersonaYClienteUsandoJoined() {
        ClienteEntity cliente = cliente("CLI-001", "1234567890");

        clienteJpaRepository.saveAndFlush(cliente);

        Number personas = (Number) entityManager
                .createNativeQuery("select count(*) from personas")
                .getSingleResult();
        Number clientes = (Number) entityManager
                .createNativeQuery("select count(*) from clientes")
                .getSingleResult();

        assertEquals(1, personas.intValue());
        assertEquals(1, clientes.intValue());
        assertTrue(clienteJpaRepository.findByClienteId("CLI-001").isPresent());
    }

    @Test
    void saveDebeRechazarClienteIdDuplicado() {
        clienteJpaRepository.saveAndFlush(cliente("CLI-001", "1234567890"));

        assertThrows(DataIntegrityViolationException.class, () ->
                clienteJpaRepository.saveAndFlush(cliente("CLI-001", "0987654321"))
        );
    }

    @Test
    void saveDebeRechazarIdentificacionDuplicada() {
        clienteJpaRepository.saveAndFlush(cliente("CLI-001", "1234567890"));

        assertThrows(DataIntegrityViolationException.class, () ->
                clienteJpaRepository.saveAndFlush(cliente("CLI-002", "1234567890"))
        );
    }

    private ClienteEntity cliente(String clienteId, String identificacion) {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setClienteId(clienteId);
        cliente.setNombre("Jose Lema");
        cliente.setGenero("Masculino");
        cliente.setEdad(30);
        cliente.setIdentificacion(identificacion);
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("0987654321");
        cliente.setContrasena("$2a$hash");
        cliente.setEstado(true);
        return cliente;
    }
}
