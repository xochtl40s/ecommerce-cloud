package com.ecommercecloud.tenant.repository;

import com.ecommercecloud.tenant.entity.EstadoTenant;
import com.ecommercecloud.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByCodigo(String codigo);

    Optional<Tenant> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    List<Tenant> findByEstadoOrderByCreadoEnDesc(EstadoTenant estado);

}
