package com.ecommercecloud.platform.vertical.module;

import com.ecommercecloud.platform.vertical.VerticalCode;
import com.ecommercecloud.platform.vertical.VerticalDescriptor;
import com.ecommercecloud.platform.vertical.VerticalFeature;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AbarroteCloudModule implements VerticalModule {

    @Override
    public VerticalDescriptor descriptor() {
        return new VerticalDescriptor(
                VerticalCode.ABARROTES,
                "AbarroteCloud",
                "Control total para tiendas de abarrotes",
                "Ventas, inventario, compras y cortes de caja en una sola plataforma.",
                List.of(
                        new VerticalFeature(
                                "POS",
                                "Punto de venta",
                                "Cobro rápido, carrito, ticket y control de stock.",
                                "🛒",
                                "pos"
                        ),
                        new VerticalFeature(
                                "INVENTARIO",
                                "Inventario",
                                "Existencias, mínimos, caducidades y movimientos.",
                                "📦",
                                "inventario"
                        ),
                        new VerticalFeature(
                                "PRODUCTOS",
                                "Productos",
                                "Catálogo, precios, códigos de barras y categorías.",
                                "🏷",
                                "productos"
                        ),
                        new VerticalFeature(
                                "PROVEEDORES",
                                "Proveedores",
                                "Compras, costos y seguimiento de proveedores.",
                                "🚚",
                                "proveedores"
                        ),
                        new VerticalFeature(
                                "CORTE",
                                "Corte de caja",
                                "Ventas, efectivo, diferencias y cierre diario.",
                                "💵",
                                "corte"
                        ),
                        new VerticalFeature(
                                "REPORTES",
                                "Reportes",
                                "Productos vendidos, utilidad y tendencias.",
                                "📊",
                                "reportes"
                        )
                )
        );
    }
}
