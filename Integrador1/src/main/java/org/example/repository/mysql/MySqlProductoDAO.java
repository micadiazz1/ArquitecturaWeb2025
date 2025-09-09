package org.example.repository.mysql;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.ProductoDAO;
import org.example.entity.TopProducto;

import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación MySQL de la interfaz {@link ProductoDAO}.
 * <p>
 * Se encarga de manejar las operaciones de persistencia sobre la tabla {@code Producto},
 * incluyendo la creación de la tabla, inserción de datos desde un archivo CSV,
 * y consultas específicas como el producto más vendido.
 * </p>
 */
public class MySqlProductoDAO implements ProductoDAO {
    private final Connection conn;
    private static volatile MySqlProductoDAO instance;
    /**
     * Ruta del archivo CSV que contiene los datos de productos.
     * Se utiliza para poblar la tabla {@code Producto}.
     *
     * Esto se debe arreglar, el CSVReader por alguna razon no reconoce el path.
     */
    private final String path = "src/main/java/org/example/utils/productos.csv";

    /**
     * Constructor que inicializa la conexión con la base de datos.
     *
     * @param conn conexión activa a la base de datos MySQL
     */
    private MySqlProductoDAO(Connection conn) {
        this.conn = conn;
    }

    public static MySqlProductoDAO getInstance(Connection conn) {
        if (instance == null) {
            return new MySqlProductoDAO(conn);
        }
        return instance;
    }

    /**
     * Crea la tabla {@code Producto} si no existe en la base de datos.
     * <p>
     * La tabla contiene los campos:
     * <ul>
     *     <li>idProducto (INT, PK)</li>
     *     <li>nombre (VARCHAR)</li>
     *     <li>valor (FLOAT)</li>
     * </ul>
     * </p>
     */
    @Override
    public void createTable() {
        String createProductoTable = "CREATE TABLE IF NOT EXISTS Producto (" +
                "idProducto INT PRIMARY KEY," +
                "nombre VARCHAR(45)," +
                "valor FLOAT" +
                ")";

        try (PreparedStatement statement = conn.prepareStatement(createProductoTable)) {
            statement.executeUpdate();
            System.out.println("Tabla Producto creada o ya existente");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla Producto: " + e.getMessage());
            throw new RuntimeException("Error creating Producto table", e);
        }
    }

    /**
     * Inserta los productos en la tabla {@code Producto} a partir de un archivo CSV.
     * <p>
     * Utiliza batch processing para optimizar las inserciones (en bloques de 1000 registros).
     * Controla errores de integridad y formato de datos, aplicando rollback en caso necesario.
     * </p>
     */
    @Override
    public void insertProducto() {
        try (FileReader reader = new FileReader(this.path);
             CSVParser parser = ManagerCSV.getInstance().getRecords(this.path);
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)")) {

            conn.setAutoCommit(false);

            int batchCount = 0;
            final int BATCH_SIZE = 1000;

            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idProducto")));
                statement.setString(2, row.get("nombre"));
                statement.setFloat(3, Float.parseFloat(row.get("valor")));
                statement.addBatch();

                batchCount++;

                if (batchCount % BATCH_SIZE == 0) {
                    statement.executeBatch();
                    conn.commit();
                    System.out.println("Insertados " + batchCount + " productos...");
                }
            }

            statement.executeBatch();
            conn.commit();

            System.out.println("Total de " + batchCount + " productos insertados exitosamente");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("La tabla Producto ya contiene estos datos: " + e.getMessage());
            rollbackTransaction();
        } catch (NumberFormatException e) {
            System.out.println("Error en formato numérico: " + e.getMessage());
            rollbackTransaction();
            throw new RuntimeException("Error en formato de datos del CSV", e);
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            rollbackTransaction();
            throw new RuntimeException("Error inserting productos", e);
        } finally {
            restoreAutoCommit();
        }
    }

    /**
     * Obtiene el producto con mayor recaudación total.
     * <p>
     * Calcula la cantidad vendida y la recaudación multiplicando el precio
     * por la cantidad en la relación {@code Factura_Producto}.
     * </p>
     *
     * @return instancia de {@link TopProducto} con el producto top, o {@code null} si no hay ventas
     */
    @Override
    public TopProducto getTopProduct() {
        String query = "SELECT p.nombre, p.valor as valor_unitario, " +
                "COUNT(fp.idProducto) AS cantidad_vendida, " +
                "COUNT(fp.idProducto) * p.valor AS recaudacion_total " +
                "FROM Producto p " +
                "JOIN Factura_Producto fp ON p.idProducto = fp.idProducto " +
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
            throw new RuntimeException("Error al obtener el producto top", e);
        }
    }

    /**
     * Realiza un rollback de la transacción actual.
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
     * Restaura el modo de auto-commit en la conexión.
     */
    private void restoreAutoCommit() {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Error al restaurar auto-commit: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los productos de la tabla {@code Producto}.
     * <p>
     * Este método está pensado para debugging y muestra los productos
     * recuperados en consola.
     * </p>
     *
     * @return lista de productos como {@link TopProducto} (aunque no se completa con recaudación)
     */
    public List<TopProducto> getAllProducts() {
        List<TopProducto> productos = new ArrayList<>();
        String query = "SELECT idProducto, nombre, valor FROM Producto";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println("Producto: " + rs.getString("nombre") +
                        ", Valor: " + rs.getFloat("valor"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }
}
