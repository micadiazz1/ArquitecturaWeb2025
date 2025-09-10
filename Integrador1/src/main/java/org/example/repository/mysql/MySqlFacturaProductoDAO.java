package org.example.repository.mysql;

import org.example.DAO.FacturaProductoDAO;

import org.example.entity.FacturaProducto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MySqlFacturaProductoDAO implements FacturaProductoDAO {

    /** Conexión activa con la base de datos */
    private final Connection conn;

    /**
     * Constructor de clase.
     * @param conn conexión a la base de datos MySQL
     */
    public MySqlFacturaProductoDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserta una lista de relaciones factura-producto en la base de datos.
     * <p>
     * Utiliza procesamiento en lotes (batch) y transacciones para optimizar
     * el rendimiento y garantizar la consistencia de los datos.
     *
     * @param facturasProductos lista de objetos FacturaProducto a insertar.
     */
    @Override
    public void insertAll(List<FacturaProducto> facturasProductos) {
        String sql = "INSERT INTO factura_producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (FacturaProducto fp : facturasProductos) {
                statement.setInt(1, fp.getIdFactura());
                statement.setInt(2, fp.getIdProducto());
                statement.setInt(3, fp.getCantidad());
                statement.addBatch();
            }
            statement.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("Insertados " + facturasProductos.size() + " registros en Factura_Producto.");
        } catch (SQLException e) {
            System.err.println("Error al insertar Factura-Producto en batch: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
        }
    }
}