package org.example.repository.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.FacturaProductoDAO;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class MySqlFacturaProductoDAO implements FacturaProductoDAO {
    private final Connection conn;
    //falta arreglar el tema de que no reconoce las rutas
    //src/main/java/org/example/utils/facturas-productos
    private final String path = "org/example/utils/facturas-productos";

    public MySqlFacturaProductoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createTable() {
        String createFacturaProductoTable = "CREATE TABLE IF NOT EXISTS Factura_Producto (" +
                "idFactura INT," +
                "idProducto INT," +
                "cantidad INT," +
                "PRIMARY KEY (idFactura, idProducto)," +
                "FOREIGN KEY (idFactura) REFERENCES Factura(idFactura)," +
                "FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)" +
                ")";

        try (PreparedStatement statement = conn.prepareStatement(createFacturaProductoTable)) {
            statement.executeUpdate();
            System.out.println("Tabla Factura_Producto creada o ya existente");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla Factura_Producto: " + e.getMessage());
            throw new RuntimeException("Error creating Factura_Producto table", e);
        }
    }

    @Override
    public void insertFacturaProducto() {
        // Usar try-with-resources para todos los recursos
        try (FileReader reader = new FileReader("src/main/java/org.example/utils/facturas-productos.csv");
             CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Factura_Producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)")) {

            // Desactivar auto-commit para transacción
            conn.setAutoCommit(false);

            int batchCount = 0;
            final int BATCH_SIZE = 1000; // Procesar en lotes de 1000

            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idFactura")));
                statement.setInt(2, Integer.parseInt(row.get("idProducto")));
                statement.setInt(3, Integer.parseInt(row.get("cantidad")));
                statement.addBatch(); // ← Agregar al batch

                batchCount++;

                // Ejecutar batch cada BATCH_SIZE registros
                if (batchCount % BATCH_SIZE == 0) {
                    statement.executeBatch();
                    conn.commit();
                    System.out.println("Insertados " + batchCount + " registros de Factura_Producto...");
                }
            }

            // Ejecutar el batch final
            statement.executeBatch();
            conn.commit();

            System.out.println("Total de " + batchCount + " relaciones Factura-Producto insertadas exitosamente");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error de integridad referencial: " + e.getMessage());
            System.out.println("Verifica que existan las facturas y productos referenciados");
            rollbackTransaction();
        } catch (NumberFormatException e) {
            System.out.println("Error en formato de números: " + e.getMessage());
            rollbackTransaction();
            throw new RuntimeException("Error en formato de datos CSV", e);
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            rollbackTransaction();
            throw new RuntimeException("Error inserting Factura_Producto data", e);
        } finally {
            restoreAutoCommit();
        }
    }

    private void rollbackTransaction() {
        try {
            conn.rollback();
            System.out.println("Transacción revertida");
        } catch (SQLException ex) {
            System.err.println("Error al hacer rollback: " + ex.getMessage());
        }
    }

    private void restoreAutoCommit() {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Error al restaurar auto-commit: " + e.getMessage());
        }
    }
}