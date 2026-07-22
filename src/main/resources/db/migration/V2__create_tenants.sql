CREATE TABLE tenants (
    id BIGSERIAL PRIMARY KEY,

    codigo VARCHAR(50) NOT NULL UNIQUE,

    nombre_comercial VARCHAR(150) NOT NULL,

    razon_social VARCHAR(200),

    propietario VARCHAR(150) NOT NULL,

    correo VARCHAR(150) NOT NULL,

    telefono VARCHAR(30),

    ciudad VARCHAR(100),

    estado VARCHAR(30) NOT NULL DEFAULT 'PROSPECTO',

    vertical_id BIGINT NOT NULL,

    plan_id BIGINT NOT NULL,

    fecha_activacion DATE,

    fecha_vencimiento DATE,

    activo BOOLEAN NOT NULL DEFAULT FALSE,

    creado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    actualizado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tenant_vertical
        FOREIGN KEY (vertical_id)
        REFERENCES verticales(id),

    CONSTRAINT fk_tenant_plan
        FOREIGN KEY (plan_id)
        REFERENCES planes(id),

    CONSTRAINT chk_tenant_estado
        CHECK (
            estado IN (
                'PROSPECTO',
                'PENDIENTE_PAGO',
                'ACTIVO',
                'SUSPENDIDO',
                'VENCIDO',
                'CANCELADO'
            )
        )
);

CREATE INDEX idx_tenants_codigo
    ON tenants(codigo);

CREATE INDEX idx_tenants_correo
    ON tenants(correo);

CREATE INDEX idx_tenants_estado
    ON tenants(estado);

CREATE INDEX idx_tenants_vertical
    ON tenants(vertical_id);

CREATE INDEX idx_tenants_plan
    ON tenants(plan_id);
