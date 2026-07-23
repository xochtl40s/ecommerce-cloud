CREATE TABLE password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,

    account_type VARCHAR(30) NOT NULL,

    platform_user_id BIGINT,

    tenant_user_id BIGINT,

    token_hash VARCHAR(64) NOT NULL UNIQUE,

    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,

    used_at TIMESTAMP WITH TIME ZONE,

    requested_ip VARCHAR(80),

    created_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_password_reset_platform_user
        FOREIGN KEY (platform_user_id)
        REFERENCES platform_users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_password_reset_tenant_user
        FOREIGN KEY (tenant_user_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_password_reset_account_type
        CHECK (
            account_type IN (
                'PLATFORM',
                'TENANT'
            )
        ),

    CONSTRAINT chk_password_reset_owner
        CHECK (
            (
                platform_user_id IS NOT NULL
                AND tenant_user_id IS NULL
                AND account_type = 'PLATFORM'
            )
            OR
            (
                platform_user_id IS NULL
                AND tenant_user_id IS NOT NULL
                AND account_type = 'TENANT'
            )
        )
);

CREATE INDEX idx_password_reset_platform_user
    ON password_reset_tokens(platform_user_id);

CREATE INDEX idx_password_reset_tenant_user
    ON password_reset_tokens(tenant_user_id);

CREATE INDEX idx_password_reset_expires_at
    ON password_reset_tokens(expires_at);

CREATE INDEX idx_password_reset_used_at
    ON password_reset_tokens(used_at);
