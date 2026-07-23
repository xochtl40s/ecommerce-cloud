package com.ecommercecloud.platform.vertical.module;

import com.ecommercecloud.platform.vertical.VerticalCode;
import com.ecommercecloud.platform.vertical.VerticalDescriptor;
import com.ecommercecloud.platform.vertical.VerticalFeature;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoffeeShopCloudModule implements VerticalModule {

    @Override
    public VerticalDescriptor descriptor() {
        return new VerticalDescriptor(
                VerticalCode.COFFEE_SHOP,
                "CoffeeShopCloud",
                "Ventas rápidas para cafeterías",
                "Bebidas, tamaños, extras, órdenes e inventario de insumos.",
                List.of(
                        new VerticalFeature("POS", "Punto de venta", "Cobro rápido para mostrador.", "☕", "pos"),
                        new VerticalFeature("BEBIDAS", "Bebidas", "Recetas, tamaños y disponibilidad.", "🥤", "bebidas"),
                        new VerticalFeature("EXTRAS", "Extras", "Leches, jarabes, toppings y complementos.", "➕", "extras"),
                        new VerticalFeature("ORDENES", "Órdenes", "Cola de preparación y entrega.", "🧾", "ordenes"),
                        new VerticalFeature("INSUMOS", "Insumos", "Café, leche, vasos y consumibles.", "📦", "insumos"),
                        new VerticalFeature("CORTE", "Corte de caja", "Ventas por turno y método de pago.", "💵", "corte")
                )
        );
    }
}
