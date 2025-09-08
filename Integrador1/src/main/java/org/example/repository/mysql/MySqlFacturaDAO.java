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

/**
 * Implementación de {@link FacturaDAO} para MySQL usando JDBC.
 * <p>
 * Este DAO permite:
 * <ul>
 *   <li>Crear la tabla {@code Factura}</li>
 *   <li>Insertar facturas desde un archivo CSV</li>
 *   <li>Manejar transacciones y errores de integridad referencial</li>
 * </ul>
 *
 * El archivo CSV debe tener las columnas:
 * <pre>
 *   idFactura, idCliente
 * </pre>
 */
public class MySqlFacturaDAO implements FacturaDAO {

    /** Conexión activa con la base de datos MySQL */
    private final Connection connection;
    private static volatile MySqlFacturaDAO instance;
    /**
     * Ruta al archivo CSV de facturas
     * Esto se debe arreglar, el CSVReader por alguna razon no reconoce el path.
     * */
    private final String path = "Integrador1/src/main/java/org/example/utils/facturas.csv";

    /**
     * Constructor de clase.
     *
     * @param connection conexión a la base de datos MySQL
     */
    private MySqlFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    public static MySqlFacturaDAO getInstance(Connection connection) {
        if (instance == null) {
            return new MySqlFacturaDAO(connection);
        }
        return instance;
    }

    /**
     * Crea la tabla {@code Factura} en la base de datos si no existe.
     * <p>
     * Estructura de la tabla:
     * <ul>
     *   <li>{@code idFactura} (INT, PK)</li>
     *   <li>{@code idCliente} (INT, FK a Cliente)</li>
     * </ul>
     *
     * @throws RuntimeException si ocurre un error al crear la tabla
     */
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

    /**
     * Inserta facturas en la base de datos desde un archivo CSV.
     * <p>
     * El proceso se ejecuta dentro de una transacción y utiliza batch processing
     * para mejorar el rendimiento en cargas grandes de datos.
     * <p>
     * El archivo debe tener cabeceras {@code idFactura} y {@code idCliente}.
     *
     * @throws RuntimeException si ocurre un error de SQL, integridad referencial o formato del CSV
     */
    @Override
    public void insertFactura() {
        try (FileReader reader = new FileReader(this.path);
             CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)")) {

            connection.setAutoCommit(false);

            int batchCount = 0;
            final int BATCH_SIZE = 1000;

            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idFactura")));
                statement.setInt(2, Integer.parseInt(row.get("idCliente")));
                statement.addBatch();

                batchCount++;

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

    /**
     * Revierte la transacción activa en caso de error.
     */
    private void rollbackTransaction() {
        try {
            connection.rollback();
            System.out.println("Transacción revertida");
        } catch (SQLException ex) {
            System.err.println("Error al hacer rollback: " + ex.getMessage());
        }
    }

    /**
     * Restaura el modo {@code autoCommit} de la conexión a su valor por defecto.
     */
    private void restoreAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Error al restaurar auto-commit: " + e.getMessage());
        }
    }
}
