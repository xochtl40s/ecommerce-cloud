package com.ecommercecloud.platform.vertical.module;

import com.ecommercecloud.platform.vertical.VerticalCode;
import com.ecommercecloud.platform.vertical.VerticalDescriptor;
import com.ecommercecloud.platform.vertical.VerticalFeature;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantCloudModule implements VerticalModule {

    @Override
    public VerticalDescriptor descriptor() {
        return new VerticalDescriptor(
                VerticalCode.RESTAURANTES,
                "RestauranteCloud",
                "Operación ágil para restaurantes",
                "Mesas, comandas, cocina, meseros, caja y reportes conectados.",
                List.of(
                        new VerticalFeature("MESAS", "Mesas", "Disponibilidad y cuenta por mesa.", "🪑", "mesas"),
                        new VerticalFeature("COMANDAS", "Comandas", "Pedidos desde celular por mesero.", "🧾", "comandas"),
                        new VerticalFeature("COCINA", "Cocina", "Órdenes pendientes, preparación y entrega.", "🍳", "cocina"),
                        new VerticalFeature("MENU", "Menú", "Platillos, categorías, extras y disponibilidad.", "🍽", "menu"),
                        new VerticalFeature("CAJA", "Caja", "Cobro, división de cuenta y corte diario.", "💳", "caja"),
                        new VerticalFeature("MESEROS", "Meseros", "Ventas, productividad y seguimiento.", "👤", "meseros")
                )
        );
    }
}
