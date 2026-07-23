package com.ecommercecloud.platform.workspace;

import com.ecommercecloud.platform.vertical.VerticalModuleRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TenantWorkspaceController {

    private final TenantWorkspaceService workspaceService;
    private final VerticalModuleRegistry moduleRegistry;

    public TenantWorkspaceController(
            TenantWorkspaceService workspaceService,
            VerticalModuleRegistry moduleRegistry
    ) {
        this.workspaceService = workspaceService;
        this.moduleRegistry = moduleRegistry;
    }

    @GetMapping("/workspace/{tenantCode}")
    public String home(
            @PathVariable String tenantCode,
            Model model
    ) {
        model.addAttribute(
                "workspace",
                workspaceService.load(tenantCode)
        );

        return "workspace/home";
    }

    @GetMapping("/api/public/verticals")
    @ResponseBody
    public Object verticals() {
        return moduleRegistry.listAll();
    }
}
