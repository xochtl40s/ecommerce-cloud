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

    long countByEstado(EstadoTenant estado);

    List<Tenant> findByEstadoOrderByCreadoEnDesc(
            EstadoTenant estado
    );

    List<Tenant> findTop5ByOrderByCreadoEnDesc();

    List<Tenant> findAllByOrderByCreadoEnDesc();

    List<Tenant> findByNombreComercialContainingIgnoreCaseOrCodigoContainingIgnoreCaseOrCorreoContainingIgnoreCaseOrPropietarioContainingIgnoreCaseOrderByCreadoEnDesc(
            String nombreComercial,
            String codigo,
            String correo,
            String propietario
    );

    List<Tenant> findByEstadoAndNombreComercialContainingIgnoreCaseOrEstadoAndCodigoContainingIgnoreCaseOrEstadoAndCorreoContainingIgnoreCaseOrEstadoAndPropietarioContainingIgnoreCaseOrderByCreadoEnDesc(
            EstadoTenant estadoNombre,
            String nombreComercial,
            EstadoTenant estadoCodigo,
            String codigo,
            EstadoTenant estadoCorreo,
            String correo,
            EstadoTenant estadoPropietario,
            String propietario
    );
}
