CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,

    username VARCHAR(100) NOT NULL,

    password_hash VARCHAR(255) NOT NULL,

    nombre VARCHAR(150) NOT NULL,

    correo VARCHAR(150) NOT NULL,

    rol VARCHAR(30) NOT NULL,

    activo BOOLEAN NOT NULL DEFAULT TRUE,

    cambio_password_requerido BOOLEAN NOT NULL DEFAULT TRUE,

    creado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    actualizado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_usuario_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id),

    CONSTRAINT uk_usuario_tenant_username
        UNIQUE (tenant_id, username),

    CONSTRAINT chk_usuario_rol
        CHECK (
            rol IN (
                'TENANT_ADMIN',
                'GERENTE',
                'CAJERO',
                'MESERO',
                'ENTRENADOR',
                'USUARIO'
            )
        )
);

CREATE INDEX idx_usuarios_tenant
    ON usuarios(tenant_id);

CREATE INDEX idx_usuarios_correo
    ON usuarios(correo);

CREATE INDEX idx_usuarios_rol
    ON usuarios(rol);
