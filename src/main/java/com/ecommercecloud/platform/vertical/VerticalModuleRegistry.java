package com.ecommercecloud.platform.vertical;

import com.ecommercecloud.platform.vertical.module.VerticalModule;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class VerticalModuleRegistry {

    private final Map<VerticalCode, VerticalDescriptor> modules;

    public VerticalModuleRegistry(
            List<VerticalModule> verticalModules
    ) {
        EnumMap<VerticalCode, VerticalDescriptor> registry =
                new EnumMap<>(VerticalCode.class);

        verticalModules.forEach(module -> {
            VerticalDescriptor descriptor =
                    module.descriptor();

            registry.put(
                    descriptor.code(),
                    descriptor
            );
        });

        this.modules = Map.copyOf(registry);
    }

    public VerticalDescriptor get(VerticalCode code) {

        VerticalDescriptor descriptor =
                modules.get(code);

        if (descriptor != null) {
            return descriptor;
        }

        return modules.get(VerticalCode.OTROS);
    }

    public List<VerticalDescriptor> listAll() {
        return modules.values()
                .stream()
                .sorted((left, right) ->
                        left.productName()
                                .compareToIgnoreCase(
                                        right.productName()
                                )
                )
                .toList();
    }
}
