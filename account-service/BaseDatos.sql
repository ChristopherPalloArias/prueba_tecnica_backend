-- Script de base de datos para account-service.
-- Estrategia de entrega: cada microservicio mantiene su propio BaseDatos.sql.
-- En Docker Compose se ejecuta al inicializar el contenedor PostgreSQL account-db.

CREATE DATABASE account_db;

\c account_db;

CREATE TABLE clientes_replica (
    cliente_id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    identificacion VARCHAR(30) NOT NULL,
    estado BOOLEAN NOT NULL
);

CREATE TABLE cuentas (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(50) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(30) NOT NULL,
    saldo_inicial NUMERIC(19, 2) NOT NULL CHECK (saldo_inicial >= 0),
    saldo_disponible NUMERIC(19, 2) NOT NULL CHECK (saldo_disponible >= 0),
    estado BOOLEAN NOT NULL,
    cliente_id VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE movimientos (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor NUMERIC(19, 2) NOT NULL CHECK (valor <> 0),
    saldo NUMERIC(19, 2) NOT NULL,
    estado BOOLEAN NOT NULL,
    cuenta_id BIGINT NOT NULL,
    descripcion VARCHAR(200),
    CONSTRAINT fk_movimientos_cuentas
        FOREIGN KEY (cuenta_id)
        REFERENCES cuentas (id)
);

CREATE INDEX idx_cuentas_cliente_id ON cuentas (cliente_id);
CREATE INDEX idx_movimientos_cuenta_fecha ON movimientos (cuenta_id, fecha);
