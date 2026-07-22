package com.ecommercecloud.publicsite.controller;

import com.ecommercecloud.platform.vertical.VerticalCode;
import com.ecommercecloud.platform.vertical.VerticalDescriptor;
import com.ecommercecloud.platform.vertical.VerticalModuleRegistry;
import com.ecommercecloud.publicsite.service.PublicLandingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PublicSiteController {

    private final PublicLandingService landingService;
    private final VerticalModuleRegistry verticalModuleRegistry;

    public PublicSiteController(
            PublicLandingService landingService,
            VerticalModuleRegistry verticalModuleRegistry
    ) {
        this.landingService = landingService;
        this.verticalModuleRegistry = verticalModuleRegistry;
    }

    @GetMapping({
            "/",
            "/inicio"
    })
    public String inicio(Model model) {

        model.addAttribute(
                "verticales",
                landingService.listarVerticales()
        );

        return "public/index";
    }

    @GetMapping("/soluciones/{codigo}")
    public String solucion(
            @PathVariable String codigo,
            Model model
    ) {

        VerticalCode verticalCode =
                VerticalCode.fromDatabase(codigo);

        VerticalDescriptor vertical =
                verticalModuleRegistry.get(verticalCode);

        model.addAttribute("vertical", vertical);

        return "public/solucion";
    }
}
