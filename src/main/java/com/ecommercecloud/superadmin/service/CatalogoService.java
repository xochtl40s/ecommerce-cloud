package com.ecommercecloud.superadmin.service;

import com.ecommercecloud.superadmin.dto.CatalogoItemView;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CatalogoService {

    private final JdbcClient jdbcClient;

    public CatalogoService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<CatalogoItemView> listarVerticales() {
        return jdbcClient.sql("""
                        SELECT id, codigo, nombre
                        FROM verticales
                        WHERE activo = TRUE
                        ORDER BY nombre
                        """)
                .query((rs, rowNum) ->
                        new CatalogoItemView(
                                rs.getLong("id"),
                                rs.getString("codigo"),
                                rs.getString("nombre")
                        )
                )
                .list();
    }

    public List<CatalogoItemView> listarPlanes() {
        return jdbcClient.sql("""
                        SELECT id, codigo, nombre
                        FROM planes
                        WHERE activo = TRUE
                        ORDER BY precio_mensual
                        """)
                .query((rs, rowNum) ->
                        new CatalogoItemView(
                                rs.getLong("id"),
                                rs.getString("codigo"),
                                rs.getString("nombre")
                        )
                )
                .list();
    }

    public String obtenerNombreVertical(Long id) {
        return jdbcClient.sql("""
                        SELECT nombre
                        FROM verticales
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(String.class)
                .optional()
                .orElse("Sin vertical");
    }

    public String obtenerNombrePlan(Long id) {
        return jdbcClient.sql("""
                        SELECT nombre
                        FROM planes
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(String.class)
                .optional()
                .orElse("Sin plan");
    }
}
