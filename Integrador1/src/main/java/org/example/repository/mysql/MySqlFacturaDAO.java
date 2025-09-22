package org.example.repository.mysql;


import org.example.DAO.FacturaDAO;
import org.example.entity.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MySqlFacturaDAO implements FacturaDAO {

    /** Conexión activa con la base de datos MySQL */
    private final Connection connection;

    /**
     * Constructor de clase.
     *
     * @param connection conexión a la base de datos MySQL
     */
    public MySqlFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserta una lista de facturas en la base de datos.
     * <p>
     * El proceso se ejecuta dentro de una transacción y utiliza batch processing.
     *
     * @param facturas lista de objetos Factura a insertar.
     */
    @Override
    public void insertAll(List<Factura> facturas) {
        String sql = "INSERT INTO factura (idFactura, idCliente) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            for (Factura factura : facturas) {
                statement.setInt(1, factura.getIdFactura());
                statement.setInt(2, factura.getIdCliente());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("Insertados " + facturas.size() + " registros en Factura.");
        } catch (SQLException e) {
            System.err.println("Error al insertar facturas en batch: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
        }
    }
}

