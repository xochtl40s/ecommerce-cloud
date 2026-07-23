package com.ecommercecloud.platform.vertical.module;

import com.ecommercecloud.platform.vertical.VerticalCode;
import com.ecommercecloud.platform.vertical.VerticalDescriptor;
import com.ecommercecloud.platform.vertical.VerticalFeature;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenericCloudModule implements VerticalModule {

    @Override
    public VerticalDescriptor descriptor() {
        return new VerticalDescriptor(
                VerticalCode.OTROS,
                "BusinessCloud",
                "Base configurable para nuevos rubros",
                "Módulo inicial reutilizable para incorporar nuevos verticales.",
                List.of(
                        new VerticalFeature("VENTAS", "Ventas", "Registro de operaciones e ingresos.", "🛍", "ventas"),
                        new VerticalFeature("CLIENTES", "Clientes", "Directorio y seguimiento comercial.", "👥", "clientes"),
                        new VerticalFeature("CATALOGO", "Catálogo", "Productos o servicios configurables.", "📚", "catalogo"),
                        new VerticalFeature("PAGOS", "Pagos", "Cobros y movimientos.", "💳", "pagos"),
                        new VerticalFeature("REPORTES", "Reportes", "Indicadores básicos del negocio.", "📊", "reportes")
                )
        );
    }
}
