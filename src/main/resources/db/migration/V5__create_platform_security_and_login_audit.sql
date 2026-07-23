CREATE TABLE platform_users (
    id BIGSERIAL PRIMARY KEY,

    username VARCHAR(100) NOT NULL UNIQUE,

    password_hash VARCHAR(255) NOT NULL,

    nombre VARCHAR(150) NOT NULL,

    correo VARCHAR(150) NOT NULL UNIQUE,

    rol VARCHAR(30) NOT NULL DEFAULT 'SUPER_ADMIN',

    activo BOOLEAN NOT NULL DEFAULT TRUE,

    cambio_password_requerido BOOLEAN NOT NULL DEFAULT TRUE,

    creado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    actualizado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_platform_user_role
        CHECK (
            rol IN (
                'SUPER_ADMIN',
                'SOPORTE',
                'VENTAS'
            )
        )
);

CREATE INDEX idx_platform_users_username
    ON platform_users(username);

CREATE INDEX idx_platform_users_correo
    ON platform_users(correo);

CREATE INDEX idx_platform_users_activo
    ON platform_users(activo);


CREATE TABLE login_audit (
    id BIGSERIAL PRIMARY KEY,

    username VARCHAR(150),

    account_type VARCHAR(30),

    tenant_id BIGINT,

    success BOOLEAN NOT NULL,

    ip_address VARCHAR(80),

    user_agent VARCHAR(500),

    failure_reason VARCHAR(255),

    created_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_login_audit_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_login_audit_account_type
        CHECK (
            account_type IS NULL
            OR account_type IN (
                'PLATFORM',
                'TENANT'
            )
        )
);

CREATE INDEX idx_login_audit_username
    ON login_audit(username);

CREATE INDEX idx_login_audit_tenant
    ON login_audit(tenant_id);

CREATE INDEX idx_login_audit_created_at
    ON login_audit(created_at DESC);

CREATE INDEX idx_login_audit_success
    ON login_audit(success);
