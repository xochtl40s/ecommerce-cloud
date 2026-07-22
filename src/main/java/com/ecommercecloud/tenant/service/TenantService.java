package com.ecommercecloud.tenant.service;

import com.ecommercecloud.tenant.dto.TenantCreateRequest;
import com.ecommercecloud.tenant.dto.TenantResponse;
import com.ecommercecloud.tenant.entity.EstadoTenant;

import java.util.List;

public interface TenantService {

    TenantResponse crearProspecto(TenantCreateRequest request);

    TenantResponse obtenerPorId(Long id);

    List<TenantResponse> listarTodos();

    List<TenantResponse> listarPorEstado(EstadoTenant estado);
}
