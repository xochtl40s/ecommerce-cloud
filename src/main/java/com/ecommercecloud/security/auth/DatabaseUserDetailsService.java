package com.ecommercecloud.security.auth;

import com.ecommercecloud.security.entity.Usuario;
import com.ecommercecloud.security.platform.entity.PlatformUser;
import com.ecommercecloud.security.platform.repository.PlatformUserRepository;
import com.ecommercecloud.security.repository.UsuarioRepository;
import com.ecommercecloud.tenant.entity.EstadoTenant;
import com.ecommercecloud.tenant.entity.Tenant;
import com.ecommercecloud.tenant.repository.TenantRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DatabaseUserDetailsService
        implements UserDetailsService {

    private final PlatformUserRepository
            platformUserRepository;

    private final UsuarioRepository
            usuarioRepository;

    private final TenantRepository
            tenantRepository;

    public DatabaseUserDetailsService(
            PlatformUserRepository platformUserRepository,
            UsuarioRepository usuarioRepository,
            TenantRepository tenantRepository
    ) {
        this.platformUserRepository =
                platformUserRepository;

        this.usuarioRepository =
                usuarioRepository;

        this.tenantRepository =
                tenantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {

        String normalizedUsername =
                username == null
                        ? ""
                        : username.trim();

        PlatformUser platformUser =
                platformUserRepository
                        .findByUsernameIgnoreCase(
                                normalizedUsername
                        )
                        .orElse(null);

        if (platformUser != null) {
            return createPlatformPrincipal(
                    platformUser
            );
        }

        Usuario usuario =
                usuarioRepository
                        .findByUsernameIgnoreCase(
                                normalizedUsername
                        )
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuario o contraseña incorrectos"
                                )
                        );

        Tenant tenant =
                tenantRepository
                        .findById(usuario.getTenantId())
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "La empresa asociada no existe"
                                )
                        );

        boolean tenantOperational =
                Boolean.TRUE.equals(tenant.getActivo())
                && tenant.getEstado()
                == EstadoTenant.ACTIVO;

        boolean accountActive =
                Boolean.TRUE.equals(usuario.getActivo())
                && tenantOperational;

        return ECommercePrincipal.tenant(
                usuario.getId(),
                tenant.getId(),
                tenant.getCodigo(),
                tenant.getNombreComercial(),
                usuario.getUsername(),
                usuario.getPasswordHash(),
                usuario.getNombre(),
                usuario.getRol().name(),
                accountActive,
                Boolean.TRUE.equals(
                        usuario.getCambioPasswordRequerido()
                )
        );
    }

    private ECommercePrincipal createPlatformPrincipal(
            PlatformUser user
    ) {
        return ECommercePrincipal.platform(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getNombre(),
                user.getRol(),
                Boolean.TRUE.equals(user.getActivo()),
                Boolean.TRUE.equals(
                        user.getCambioPasswordRequerido()
                )
        );
    }
}
