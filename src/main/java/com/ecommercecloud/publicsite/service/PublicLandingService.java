package com.ecommercecloud.publicsite.service;

import com.ecommercecloud.platform.vertical.VerticalDescriptor;
import com.ecommercecloud.platform.vertical.VerticalModuleRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicLandingService {

    private final VerticalModuleRegistry verticalModuleRegistry;

    public PublicLandingService(
            VerticalModuleRegistry verticalModuleRegistry
    ) {
        this.verticalModuleRegistry = verticalModuleRegistry;
    }

    public List<VerticalDescriptor> listarVerticales() {
        return verticalModuleRegistry.listAll();
    }
}
