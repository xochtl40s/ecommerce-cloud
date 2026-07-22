package com.ecommercecloud.security.repository;

import com.ecommercecloud.security.entity.RolUsuario;
import com.ecommercecloud.security.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    boolean existsByTenantIdAndRol(
            Long tenantId,
            RolUsuario rol
    );

    Optional<Usuario> findByTenantIdAndUsername(
            Long tenantId,
            String username
    );

    Optional<Usuario> findByUsernameIgnoreCase(
            String username
    );
}
