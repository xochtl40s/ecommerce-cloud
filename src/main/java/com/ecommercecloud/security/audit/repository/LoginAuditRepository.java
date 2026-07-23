package com.ecommercecloud.security.audit.repository;

import com.ecommercecloud.security.audit.entity.LoginAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAuditRepository
        extends JpaRepository<LoginAudit, Long> {
}
