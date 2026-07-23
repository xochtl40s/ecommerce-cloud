package com.ecommercecloud.platform.vertical;

import java.util.Locale;

public enum VerticalCode {

    ABARROTES,
    RESTAURANTES,
    GIMNASIOS,
    COFFEE_SHOP,
    OTROS;

    public static VerticalCode fromDatabase(String value) {

        if (value == null || value.isBlank()) {
            return OTROS;
        }

        String normalized = value
                .trim()
                .toUpperCase(Locale.ROOT)
                .replace("-", "_")
                .replace(" ", "_");

        return switch (normalized) {
            case "ABARROTE", "ABARROTES" -> ABARROTES;
            case "RESTAURANTE", "RESTAURANTES" -> RESTAURANTES;
            case "GIMNASIO", "GIMNASIOS", "GYM" -> GIMNASIOS;
            case "COFFEESHOP", "COFFEE_SHOP", "CAFETERIA", "CAFETERÍA" ->
                    COFFEE_SHOP;
            default -> OTROS;
        };
    }
}
