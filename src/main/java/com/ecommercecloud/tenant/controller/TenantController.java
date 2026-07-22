package com.ecommercecloud.tenant.controller;

import com.ecommercecloud.tenant.dto.TenantCreateRequest;
import com.ecommercecloud.tenant.dto.TenantResponse;
import com.ecommercecloud.tenant.entity.EstadoTenant;
import com.ecommercecloud.tenant.service.TenantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping
    public ResponseEntity<TenantResponse> crear(
            @Valid @RequestBody TenantCreateRequest request
    ) {
        TenantResponse response =
                tenantService.crearProspecto(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                tenantService.obtenerPorId(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<TenantResponse>> listar(
            @RequestParam(required = false) EstadoTenant estado
    ) {
        if (estado != null) {
            return ResponseEntity.ok(
                    tenantService.listarPorEstado(estado)
            );
        }

        return ResponseEntity.ok(
                tenantService.listarTodos()
        );
    }
}
