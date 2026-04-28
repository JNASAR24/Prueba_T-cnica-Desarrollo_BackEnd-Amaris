CREATE SCHEMA IF NOT EXISTS btg;

CREATE TABLE IF NOT EXISTS btg.cliente (
    id_cliente BIGSERIAL PRIMARY KEY,
    documento VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS btg.producto (
    id_producto BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    categoria VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS btg.sucursal (
    id_sucursal BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    ciudad VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS btg.inscripcion (
    id_inscripcion BIGSERIAL PRIMARY KEY,
    id_cliente BIGINT NOT NULL REFERENCES btg.cliente(id_cliente),
    id_producto BIGINT NOT NULL REFERENCES btg.producto(id_producto),
    fecha_inscripcion TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (id_cliente, id_producto)
);

CREATE TABLE IF NOT EXISTS btg.disponibilidad (
    id_disponibilidad BIGSERIAL PRIMARY KEY,
    id_producto BIGINT NOT NULL REFERENCES btg.producto(id_producto),
    id_sucursal BIGINT NOT NULL REFERENCES btg.sucursal(id_sucursal),
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (id_producto, id_sucursal)
);

CREATE TABLE IF NOT EXISTS btg.visitan (
    id_visita BIGSERIAL PRIMARY KEY,
    id_cliente BIGINT NOT NULL REFERENCES btg.cliente(id_cliente),
    id_sucursal BIGINT NOT NULL REFERENCES btg.sucursal(id_sucursal),
    fecha_visita TIMESTAMP NOT NULL DEFAULT NOW()
);
