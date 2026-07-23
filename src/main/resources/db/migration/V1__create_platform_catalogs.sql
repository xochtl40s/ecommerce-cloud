CREATE TABLE verticales (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE planes (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio_mensual NUMERIC(12, 2) NOT NULL DEFAULT 0,
    limite_usuarios INTEGER,
    limite_sucursales INTEGER,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO verticales (codigo, nombre, descripcion)
VALUES
    ('ABARROTES', 'Abarrotes', 'Punto de venta e inventario'),
    ('RESTAURANTES', 'Restaurantes', 'Mesas, comandas, cocina y caja'),
    ('GIMNASIOS', 'Gimnasios', 'Clientes, membresías y asistencias');

INSERT INTO planes (
    codigo,
    nombre,
    descripcion,
    precio_mensual,
    limite_usuarios,
    limite_sucursales
)
VALUES
    ('BASIC', 'Básico', 'Plan inicial para pequeños negocios', 299.00, 3, 1),
    ('PRO', 'Profesional', 'Plan para negocios en crecimiento', 599.00, 10, 3),
    ('ENTERPRISE', 'Empresarial', 'Plan para organizaciones avanzadas', 1499.00, NULL, NULL);
