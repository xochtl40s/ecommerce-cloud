package com.ecommercecloud.security.audit.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "login_audit")
public class LoginAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String username;

    @Column(
            name = "account_type",
            length = 30
    )
    private String accountType;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(nullable = false)
    private Boolean success;

    @Column(
            name = "ip_address",
            length = 80
    )
    private String ipAddress;

    @Column(
            name = "user_agent",
            length = 500
    )
    private String userAgent;

    @Column(
            name = "failure_reason",
            length = 255
    )
    private String failureReason;

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountType(
            String accountType
    ) {
        this.accountType = accountType;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setIpAddress(
            String ipAddress
    ) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(
            String userAgent
    ) {
        this.userAgent = userAgent;
    }

    public void setFailureReason(
            String failureReason
    ) {
        this.failureReason = failureReason;
    }
}
