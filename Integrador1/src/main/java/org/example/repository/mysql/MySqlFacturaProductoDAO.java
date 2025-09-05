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

/**
 * Implementación de {@link FacturaProductoDAO} para MySQL usando JDBC.
 * <p>
 * Este DAO permite:
 * <ul>
 *   <li>Crear la tabla intermedia {@code Factura_Producto}</li>
 *   <li>Insertar registros desde un archivo CSV con relaciones factura-producto</li>
 *   <li>Manejar transacciones y errores de integridad referencial</li>
 * </ul>
 *
 * El archivo CSV debe tener las siguientes cabeceras:
 * <pre>
 *   idFactura, idProducto, cantidad
 * </pre>
 */
public class MySqlFacturaProductoDAO implements FacturaProductoDAO {

    /** Conexión activa con la base de datos */
    private final Connection conn;

    /**
     *  Ruta al archivo CSV con las relaciones factura-producto
     *  Esto se debe arreglar, el CSVReader por alguna razon no reconoce el path.
     * */
    private final String path = "src/main/java/org/example/utils/facturas-productos.csv";

    /**
     * Constructor de clase.
     *
     * @param conn conexión a la base de datos MySQL
     */
    public MySqlFacturaProductoDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Crea la tabla intermedia {@code Factura_Producto} si no existe.
     * <p>
     * Estructura de la tabla:
     * <ul>
     *   <li>{@code idFactura} (INT, FK a Factura)</li>
     *   <li>{@code idProducto} (INT, FK a Producto)</li>
     *   <li>{@code cantidad} (INT)</li>
     *   <li>Clave primaria compuesta por {@code (idFactura, idProducto)}</li>
     * </ul>
     *
     * @throws RuntimeException si ocurre un error SQL
     */
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

    /**
     * Inserta datos de relaciones factura-producto desde un archivo CSV.
     * <p>
     * Utiliza procesamiento en lotes ({@code batch}) y transacciones
     * para optimizar el rendimiento y garantizar la consistencia de datos.
     *
     * @throws RuntimeException si ocurre un error de integridad, formato del CSV o SQL
     */
    @Override
    public void insertFacturaProducto() {
        try (FileReader reader = new FileReader(this.path);
             CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Factura_Producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)")) {

            conn.setAutoCommit(false);

            int batchCount = 0;
            final int BATCH_SIZE = 1000;

            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idFactura")));
                statement.setInt(2, Integer.parseInt(row.get("idProducto")));
                statement.setInt(3, Integer.parseInt(row.get("cantidad")));
                statement.addBatch();

                batchCount++;

                if (batchCount % BATCH_SIZE == 0) {
                    statement.executeBatch();
                    conn.commit();
                    System.out.println("Insertados " + batchCount + " registros de Factura_Producto...");
                }
            }

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

    /**
     * Revierte la transacción activa en caso de error.
     */
    private void rollbackTransaction() {
        try {
            conn.rollback();
            System.out.println("Transacción revertida");
        } catch (SQLException ex) {
            System.err.println("Error al hacer rollback: " + ex.getMessage());
        }
    }

    /**
     * Restaura el valor por defecto de {@code autoCommit}.
     */
    private void restoreAutoCommit() {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Error al restaurar auto-commit: " + e.getMessage());
        }
    }
}
