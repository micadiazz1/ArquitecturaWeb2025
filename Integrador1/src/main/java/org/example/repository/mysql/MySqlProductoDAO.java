package org.example.repository.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.ProductoDAO;
import org.example.entity.TopProducto;

import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlProductoDAO implements ProductoDAO {
    private final Connection conn;
    //falta arreglar el tema de que no reconoce las rutas
    //src/main/java/org/example/utils/productos.csv
    private final String path = "src/main/java/org/example/utils/productos.csv";

    public MySqlProductoDAO(Connection conn) {
        this.conn = conn;
    }

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

    @Override
    public void insertProducto() {
        // Usar try-with-resources para todos los recursos
        try (FileReader reader = new FileReader(this.path);
             CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)")) {

            // Desactivar auto-commit para transacción
            conn.setAutoCommit(false);

            int batchCount = 0;
            final int BATCH_SIZE = 1000;

            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idProducto")));
                statement.setString(2, row.get("nombre"));
                statement.setFloat(3, Float.parseFloat(row.get("valor"))); // ← Conversión correcta a float
                statement.addBatch();

                batchCount++;

                if (batchCount % BATCH_SIZE == 0) {
                    statement.executeBatch();
                    conn.commit();
                    System.out.println("Insertados " + batchCount + " productos...");
                }
            }

            // Ejecutar el batch final
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

    // Métodos auxiliares para transacciones
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

    // Método adicional útil: obtener todos los productos para debugging
    public List<TopProducto> getAllProducts() {
        List<TopProducto> productos = new ArrayList<>();
        String query = "SELECT idProducto, nombre, valor FROM Producto";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Adaptar según tu constructor de TopProducto
                System.out.println("Producto: " + rs.getString("nombre") +
                        ", Valor: " + rs.getFloat("valor"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }
}