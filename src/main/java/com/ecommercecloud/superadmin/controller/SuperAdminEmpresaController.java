package com.ecommercecloud.superadmin.controller;

import com.ecommercecloud.common.exception.BusinessException;
import com.ecommercecloud.superadmin.dto.TenantCreateForm;
import com.ecommercecloud.superadmin.service.CatalogoService;
import com.ecommercecloud.superadmin.service.SuperAdminEmpresaService;
import com.ecommercecloud.tenant.dto.TenantActivationResponse;
import com.ecommercecloud.tenant.dto.TenantResponse;
import com.ecommercecloud.tenant.entity.EstadoTenant;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/super-admin/empresas")
public class SuperAdminEmpresaController {

    private final SuperAdminEmpresaService empresaService;
    private final CatalogoService catalogoService;

    public SuperAdminEmpresaController(
            SuperAdminEmpresaService empresaService,
            CatalogoService catalogoService
    ) {
        this.empresaService = empresaService;
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) EstadoTenant estado,
            Model model
    ) {
        model.addAttribute(
                "empresas",
                empresaService.buscar(buscar, estado)
        );

        model.addAttribute("buscar", buscar);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("estados", EstadoTenant.values());
        model.addAttribute("paginaActual", "empresas");

        return "superadmin/empresas";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute(
                    "form",
                    new TenantCreateForm()
            );
        }

        cargarCatalogos(model);
        model.addAttribute("paginaActual", "empresas");

        return "superadmin/empresa-form";
    }

    @PostMapping
    public String registrar(
            @Valid @ModelAttribute("form")
            TenantCreateForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            cargarCatalogos(model);
            model.addAttribute(
                    "paginaActual",
                    "empresas"
            );

            return "superadmin/empresa-form";
        }

        try {
            TenantResponse creada =
                    empresaService.registrar(form);

            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "La empresa fue registrada como prospecto"
            );

            return "redirect:/super-admin/empresas/"
                    + creada.id();

        } catch (BusinessException exception) {
            model.addAttribute(
                    "mensajeError",
                    exception.getMessage()
            );

            cargarCatalogos(model);
            model.addAttribute(
                    "paginaActual",
                    "empresas"
            );

            return "superadmin/empresa-form";
        }
    }

    @GetMapping("/{id}")
    public String detalle(
            @PathVariable Long id,
            Model model
    ) {
        TenantResponse empresa =
                empresaService.obtener(id);

        model.addAttribute("empresa", empresa);

        model.addAttribute(
                "nombreVertical",
                catalogoService.obtenerNombreVertical(
                        empresa.verticalId()
                )
        );

        model.addAttribute(
                "nombrePlan",
                catalogoService.obtenerNombrePlan(
                        empresa.planId()
                )
        );

        model.addAttribute("paginaActual", "empresas");

        return "superadmin/empresa-detalle";
    }

    @PostMapping("/{id}/activar")
    public String activar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            TenantActivationResponse activacion =
                    empresaService.activar(id);

            redirectAttributes.addFlashAttribute(
                    "activacion",
                    activacion
            );

            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "La empresa fue activada correctamente"
            );

        } catch (BusinessException exception) {
            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    exception.getMessage()
            );
        }

        return "redirect:/super-admin/empresas/" + id;
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute(
                "verticales",
                catalogoService.listarVerticales()
        );

        model.addAttribute(
                "planes",
                catalogoService.listarPlanes()
        );
    }
}
