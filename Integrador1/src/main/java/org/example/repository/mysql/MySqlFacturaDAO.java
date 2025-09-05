package org.example.repository.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.FacturaDAO;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class MySqlFacturaDAO implements FacturaDAO {

    private final Connection connection;
    //falta arreglar el tema de que no reconoce las rutas
    //src/main/java/org/example/utils/facturas.csv
    private final String path = "org/example/utils/facturas.csv";

    public MySqlFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTable() {
        String createFacturaTable = "CREATE TABLE IF NOT EXISTS Factura (" +
                "idFactura INT PRIMARY KEY," +
                "idCliente INT," +
                "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)" +
                ")";

        try (PreparedStatement stmt = connection.prepareStatement(createFacturaTable)) {
            stmt.executeUpdate();
            System.out.println("Tabla Factura creada o ya existente");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla Factura: " + e.getMessage());
            throw new RuntimeException("Error creating Factura table", e);
        }
    }

    @Override
    public void insertFactura() {
        // Usar try-with-resources para todos los recursos
        try (FileReader reader = new FileReader(this.path);
             CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)")) {

            // Desactivar auto-commit para transacción
            connection.setAutoCommit(false);

            int batchCount = 0;
            final int BATCH_SIZE = 1000; // Procesar en lotes de 1000

            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idFactura")));
                statement.setInt(2, Integer.parseInt(row.get("idCliente")));
                statement.addBatch(); // ← Agregar al batch

                batchCount++;

                // Ejecutar batch cada BATCH_SIZE registros
                if (batchCount % BATCH_SIZE == 0) {
                    statement.executeBatch();
                    connection.commit();
                    System.out.println("Insertados " + batchCount + " registros...");
                }
            }

            // Ejecutar el batch final
            statement.executeBatch();
            connection.commit();

            System.out.println("Total de " + batchCount + " facturas insertadas exitosamente");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error de integridad: " + e.getMessage());
            rollbackTransaction();
        } catch (NumberFormatException e) {
            System.out.println("Error en formato de números: " + e.getMessage());
            rollbackTransaction();
            throw new RuntimeException("Error en formato de datos CSV", e);
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            rollbackTransaction();
            throw new RuntimeException("Error inserting facturas", e);
        } finally {
            restoreAutoCommit();
        }
    }

    private void rollbackTransaction() {
        try {
            connection.rollback();
            System.out.println("Transacción revertida");
        } catch (SQLException ex) {
            System.err.println("Error al hacer rollback: " + ex.getMessage());
        }
    }

    private void restoreAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Error al restaurar auto-commit: " + e.getMessage());
        }
    }
}