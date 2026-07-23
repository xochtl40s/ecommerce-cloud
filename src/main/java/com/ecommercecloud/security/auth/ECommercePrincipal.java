package com.ecommercecloud.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ECommercePrincipal implements UserDetails {

    private final Long userId;

    private final AccountType accountType;

    private final Long tenantId;

    private final String tenantCode;

    private final String businessName;

    private final String username;

    private final String password;

    private final String displayName;

    private final String role;

    private final boolean active;

    private final boolean passwordChangeRequired;

    private ECommercePrincipal(
            Long userId,
            AccountType accountType,
            Long tenantId,
            String tenantCode,
            String businessName,
            String username,
            String password,
            String displayName,
            String role,
            boolean active,
            boolean passwordChangeRequired
    ) {
        this.userId = userId;
        this.accountType = accountType;
        this.tenantId = tenantId;
        this.tenantCode = tenantCode;
        this.businessName = businessName;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.role = role;
        this.active = active;
        this.passwordChangeRequired =
                passwordChangeRequired;
    }

    public static ECommercePrincipal platform(
            Long userId,
            String username,
            String password,
            String displayName,
            String role,
            boolean active,
            boolean passwordChangeRequired
    ) {
        return new ECommercePrincipal(
                userId,
                AccountType.PLATFORM,
                null,
                null,
                "EComerce Cloud",
                username,
                password,
                displayName,
                role,
                active,
                passwordChangeRequired
        );
    }

    public static ECommercePrincipal tenant(
            Long userId,
            Long tenantId,
            String tenantCode,
            String businessName,
            String username,
            String password,
            String displayName,
            String role,
            boolean active,
            boolean passwordChangeRequired
    ) {
        return new ECommercePrincipal(
                userId,
                AccountType.TENANT,
                tenantId,
                tenantCode,
                businessName,
                username,
                password,
                displayName,
                role,
                active,
                passwordChangeRequired
        );
    }

    @Override
    public Collection<? extends GrantedAuthority>
    getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + role
                )
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public Long getUserId() {
        return userId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public boolean isPlatformUser() {
        return accountType == AccountType.PLATFORM;
    }

    public boolean isTenantUser() {
        return accountType == AccountType.TENANT;
    }

    public boolean isSuperAdmin() {
        return isPlatformUser()
                && "SUPER_ADMIN".equals(role);
    }
}
