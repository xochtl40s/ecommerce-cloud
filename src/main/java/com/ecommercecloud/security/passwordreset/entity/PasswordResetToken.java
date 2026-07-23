package com.ecommercecloud.security.passwordreset.entity;

import com.ecommercecloud.security.auth.AccountType;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "account_type",
            nullable = false,
            length = 30
    )
    private AccountType accountType;

    @Column(name = "platform_user_id")
    private Long platformUserId;

    @Column(name = "tenant_user_id")
    private Long tenantUserId;

    @Column(
            name = "token_hash",
            nullable = false,
            unique = true,
            length = 64
    )
    private String tokenHash;

    @Column(
            name = "expires_at",
            nullable = false
    )
    private OffsetDateTime expiresAt;

    @Column(name = "used_at")
    private OffsetDateTime usedAt;

    @Column(
            name = "requested_ip",
            length = 80
    )
    private String requestedIp;

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(
            AccountType accountType
    ) {
        this.accountType = accountType;
    }

    public Long getPlatformUserId() {
        return platformUserId;
    }

    public void setPlatformUserId(
            Long platformUserId
    ) {
        this.platformUserId = platformUserId;
    }

    public Long getTenantUserId() {
        return tenantUserId;
    }

    public void setTenantUserId(
            Long tenantUserId
    ) {
        this.tenantUserId = tenantUserId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(
            String tokenHash
    ) {
        this.tokenHash = tokenHash;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(
            OffsetDateTime expiresAt
    ) {
        this.expiresAt = expiresAt;
    }

    public OffsetDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(
            OffsetDateTime usedAt
    ) {
        this.usedAt = usedAt;
    }

    public String getRequestedIp() {
        return requestedIp;
    }

    public void setRequestedIp(
            String requestedIp
    ) {
        this.requestedIp = requestedIp;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isExpired() {
        return expiresAt == null
                || expiresAt.isBefore(
                        OffsetDateTime.now()
                );
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public boolean isValid() {
        return !isUsed() && !isExpired();
    }

    public void markUsed() {
        usedAt = OffsetDateTime.now();
    }
}
