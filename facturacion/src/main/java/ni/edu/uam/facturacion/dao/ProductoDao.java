package ni.edu.uam.facturacion.dao;

import ni.edu.uam.facturacion.database.ConexionDB;
import ni.edu.uam.facturacion.model.Categoria;
import ni.edu.uam.facturacion.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {

    public boolean guardar(Producto producto) throws SQLException {
        String sql = """
            INSERT INTO producto
            (
                codigo,
                nombre,
                categoria_id,
                precio_venta,
                existencia,
                activo
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setInt(3, producto.getCategoria().getId());
            ps.setBigDecimal(4, producto.getPrecioVenta());
            ps.setInt(5, producto.getExistencia());
            ps.setBoolean(6, producto.isActivo());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();

        String sql = """
                SELECT
                    p.id,
                    p.codigo,
                    p.nombre,
                    p.precio_venta,
                    p.existencia,
                    p.activo,
                    c.id AS categoria_id,
                    c.nombre AS categoria_nombre,
                    c.activa AS categoria_activa
                FROM producto p
                INNER JOIN categoria c
                    ON p.categoria_id = c.id
                ORDER BY p.id
                """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Categoria categoria = new Categoria();

                categoria.setId(rs.getInt("categoria_id"));
                categoria.setNombre(rs.getString("categoria_nombre"));
                categoria.setActiva(rs.getBoolean("categoria_activa"));

                Producto producto = new Producto();

                producto.setId(rs.getInt("id"));
                producto.setCodigo(rs.getString("codigo"));
                producto.setNombre(rs.getString("nombre"));
                producto.setCategoria(categoria);
                producto.setPrecioVenta(rs.getBigDecimal("precio_venta"));
                producto.setExistencia(rs.getInt("existencia"));
                producto.setActivo(rs.getBoolean("activo"));

                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }

        return productos;
    }

    public boolean actualizar(Producto producto) throws SQLException {
        String sql = """
            UPDATE producto
            SET codigo = ?,
                nombre = ?,
                categoria_id = ?,
                precio_venta = ?,
                existencia = ?,
                activo = ?
            WHERE id = ?
            """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setInt(3, producto.getCategoria().getId());
            ps.setBigDecimal(4, producto.getPrecioVenta());
            ps.setInt(5, producto.getExistencia());
            ps.setBoolean(6, producto.isActivo());
            ps.setInt(7, producto.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(Integer id) throws SQLException {
        String sql = """
            DELETE FROM producto
            WHERE id = ?
            """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean existeCodigo(String codigo) throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM producto
            WHERE LOWER(codigo) = LOWER(?)
            """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public boolean existeCodigo(
            String codigo,
            Integer idExcluir
    ) throws SQLException {

        String sql = """
            SELECT COUNT(*)
            FROM producto
            WHERE LOWER(codigo) = LOWER(?)
            AND id <> ?
            """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, codigo);
            ps.setInt(2, idExcluir);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }
}