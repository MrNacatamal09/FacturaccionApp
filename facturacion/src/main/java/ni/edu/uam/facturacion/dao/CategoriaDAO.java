package ni.edu.uam.facturacion.dao;

import ni.edu.uam.facturacion.database.ConexionDB;
import ni.edu.uam.facturacion.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public boolean guardar(Categoria categoria) throws SQLException {
        String sql = """
            INSERT INTO categoria
            (nombre, activa)
            VALUES (?, ?)
            """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, categoria.getNombre());
            ps.setBoolean(2, categoria.isActiva());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Categoria> listar() {
        List<Categoria> categorias = new ArrayList<>();

        String sql = """
                SELECT id, nombre, activa
                FROM categoria
                ORDER BY id
                """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Categoria categoria = new Categoria();

                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setActiva(rs.getBoolean("activa"));

                categorias.add(categoria);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        }

        return categorias;
    }

    public List<Categoria> listarActivas() {
        List<Categoria> categorias = new ArrayList<>();

        String sql = """
                SELECT id, nombre, activa
                FROM categoria
                WHERE activa = TRUE
                ORDER BY nombre
                """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Categoria categoria = new Categoria();

                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setActiva(rs.getBoolean("activa"));

                categorias.add(categoria);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        }

        return categorias;
    }

    public boolean actualizar(Categoria categoria) throws SQLException {
        String sql = """
            UPDATE categoria
            SET nombre = ?, activa = ?
            WHERE id = ?
            """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, categoria.getNombre());
            ps.setBoolean(2, categoria.isActiva());
            ps.setInt(3, categoria.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = """
            DELETE FROM categoria
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

    public boolean existeNombre(String nombre) throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM categoria
            WHERE LOWER(nombre) = LOWER(?)
            """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public boolean existeNombre(
            String nombre,
            Integer idExcluir
    ) throws SQLException {

        String sql = """
            SELECT COUNT(*)
            FROM categoria
            WHERE LOWER(nombre) = LOWER(?)
            AND id <> ?
            """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, nombre);
            ps.setInt(2, idExcluir);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public boolean tieneProductos(int categoriaId) throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM producto
            WHERE categoria_id = ?
            """;

        try (
                Connection connection = ConexionDB.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, categoriaId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }
}