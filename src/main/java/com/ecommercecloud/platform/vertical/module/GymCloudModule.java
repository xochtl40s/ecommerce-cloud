package com.ecommercecloud.platform.vertical.module;

import com.ecommercecloud.platform.vertical.VerticalCode;
import com.ecommercecloud.platform.vertical.VerticalDescriptor;
import com.ecommercecloud.platform.vertical.VerticalFeature;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GymCloudModule implements VerticalModule {

    @Override
    public VerticalDescriptor descriptor() {
        return new VerticalDescriptor(
                VerticalCode.GIMNASIOS,
                "GymCloud",
                "Administración completa para gimnasios",
                "Socios, membresías, accesos, pagos y seguimiento comercial.",
                List.of(
                        new VerticalFeature("SOCIOS", "Socios", "Expediente, fotografía, contacto y estado.", "🏋", "socios"),
                        new VerticalFeature("MEMBRESIAS", "Membresías", "Planes, renovaciones y vencimientos.", "🎫", "membresias"),
                        new VerticalFeature("ASISTENCIAS", "Asistencias", "Registro de entradas y frecuencia.", "✅", "asistencias"),
                        new VerticalFeature("PAGOS", "Pagos", "Cobros, adeudos y comprobantes.", "💰", "pagos"),
                        new VerticalFeature("ENTRENADORES", "Entrenadores", "Asignaciones y seguimiento.", "💪", "entrenadores"),
                        new VerticalFeature("REPORTES", "Reportes", "Retención, ingresos y vencimientos.", "📈", "reportes")
                )
        );
    }
}
