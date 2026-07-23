package com.ecommercecloud.platform.vertical;

import java.util.List;

public record VerticalDescriptor(
        VerticalCode code,
        String productName,
        String commercialName,
        String description,
        List<VerticalFeature> features
) {
}
