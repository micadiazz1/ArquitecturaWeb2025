package org.example.repository.mysql;


import org.example.DAO.ProductoDAO;
import org.example.entity.Producto;
import org.example.entity.TopProducto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlProductoDAO implements ProductoDAO {

    private final Connection conn;

    public MySqlProductoDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserta una lista de productos en la base de datos.
     * Utiliza procesamiento en lotes (batch) para optimizar el rendimiento.
     *
     * @param productos lista de objetos Producto a insertar.
     */
    @Override
    public void insertAll(List<Producto> productos) {
        String sql = "INSERT INTO producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Producto producto : productos) {
                statement.setInt(1, producto.getIdProducto());
                statement.setString(2, producto.getNombre());
                statement.setFloat(3, producto.getValor());
                statement.addBatch();
            }
            statement.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("Insertados " + productos.size() + " productos.");
        } catch (SQLException e) {
            System.err.println("Error al insertar productos en batch: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
        }
    }

    @Override
    public TopProducto getTopProduct() {
        String query = "SELECT p.nombre, SUM(fp.cantidad) AS cantidad_vendida, " +
                "p.valor AS valor_unitario, SUM(fp.cantidad * p.valor) AS recaudacion_total " +
                "FROM producto p " +
                "JOIN factura_producto fp ON p.idProducto = fp.idProducto " +
                "GROUP BY p.idProducto, p.nombre, p.valor " +
                "ORDER BY recaudacion_total DESC " +
                "LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new TopProducto(
                        rs.getString("nombre"),
                        rs.getInt("cantidad_vendida"),
                        rs.getFloat("valor_unitario"),
                        rs.getDouble("recaudacion_total")
                );
            } else {
                System.out.println("No se encontraron productos vendidos.");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el producto top: " + e.getMessage());
            return null; // Devuelve null o lanza una excepción
        }
    }
}