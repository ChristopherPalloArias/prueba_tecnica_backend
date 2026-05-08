-- Script de base de datos para customer-service.
-- Estrategia de entrega: cada microservicio mantiene su propio BaseDatos.sql.
-- En Docker Compose se ejecuta al inicializar el contenedor PostgreSQL customer-db.

CREATE DATABASE customer_db;

\c customer_db;

CREATE TABLE personas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    genero VARCHAR(20),
    edad INTEGER,
    identificacion VARCHAR(30) NOT NULL UNIQUE,
    direccion VARCHAR(200),
    telefono VARCHAR(30)
);

CREATE TABLE clientes (
    id BIGINT PRIMARY KEY,
    cliente_id VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(120) NOT NULL,
    estado BOOLEAN NOT NULL,
    CONSTRAINT fk_clientes_personas
        FOREIGN KEY (id)
        REFERENCES personas (id)
);

CREATE INDEX idx_clientes_cliente_id ON clientes (cliente_id);
CREATE INDEX idx_personas_identificacion ON personas (identificacion);
