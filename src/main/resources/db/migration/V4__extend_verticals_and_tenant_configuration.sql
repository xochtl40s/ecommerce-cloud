INSERT INTO verticales (codigo, nombre, descripcion, activo)
VALUES
    ('COFFEE_SHOP', 'Coffee Shop', 'Ventas, bebidas, extras, órdenes e inventario', TRUE),
    ('OTROS', 'Otros giros', 'Base extensible para nuevos tipos de negocio', TRUE)
ON CONFLICT (codigo) DO UPDATE
SET nombre = EXCLUDED.nombre,
    descripcion = EXCLUDED.descripcion,
    activo = TRUE;

CREATE TABLE tenant_configuraciones (
    tenant_id BIGINT PRIMARY KEY,
    zona_horaria VARCHAR(80) NOT NULL DEFAULT 'America/Mexico_City',
    locale VARCHAR(20) NOT NULL DEFAULT 'es-MX',
    moneda VARCHAR(10) NOT NULL DEFAULT 'MXN',
    color_primario VARCHAR(20) NOT NULL DEFAULT '#2563EB',
    nombre_marca VARCHAR(150),
    creado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tenant_configuracion_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id)
        ON DELETE CASCADE
);

INSERT INTO tenant_configuraciones (tenant_id, nombre_marca)
SELECT id, nombre_comercial
FROM tenants
ON CONFLICT (tenant_id) DO NOTHING;
