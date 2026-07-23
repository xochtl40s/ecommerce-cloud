package com.ecommercecloud.security.platform.repository;

import com.ecommercecloud.security.platform.entity.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformUserRepository
        extends JpaRepository<PlatformUser, Long> {

    Optional<PlatformUser>
    findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(
            String username
    );

    boolean existsByCorreoIgnoreCase(
            String correo
    );
}
