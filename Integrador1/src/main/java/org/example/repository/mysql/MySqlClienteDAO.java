package org.example.repository.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.ClienteDAO;
import org.example.entity.Cliente;
import org.example.entity.ClienteFactura;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlClienteDAO implements ClienteDAO {
    private final Connection conn;
    //falta arreglar el tema de que no reconoce las rutas
    private final String csvPath = "src/main/java/org/example/utils/cliente.csv";

    public MySqlClienteDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createTable() {
        String create = "CREATE TABLE IF NOT EXISTS Cliente (" +
                "idCliente INT PRIMARY KEY, " +
                "nombre VARCHAR(500), " +
                "email VARCHAR(150)" +
                ")";
        try (PreparedStatement statement = conn.prepareStatement(create)) {
            statement.executeUpdate();
            System.out.println("Tabla Cliente creada o ya existente");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla Cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertCliente() {
        // Mostrar información de debug de la ruta
        File csvFile = new File(csvPath);
        System.out.println("Buscando archivo CSV en: " + csvFile.getAbsolutePath());
        System.out.println("El archivo existe: " + csvFile.exists());
        System.out.println("Es readable: " + csvFile.canRead());

        if (!csvFile.exists()) {
            System.err.println("ERROR: El archivo CSV no existe en la ruta especificada");
            System.err.println("Ruta esperada: " + csvFile.getAbsolutePath());
            return;
        }

        // Usar try-with-resources para asegurar el cierre de todos los recursos
        try (FileReader reader = new FileReader(csvFile);
             CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Cliente (idCliente, nombre, email) VALUES (?, ?, ?)")) {

            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false); // ← Iniciar transacción para inserción masiva

            int recordCount = 0;
            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idCliente")));
                statement.setString(2, row.get("nombre"));
                statement.setString(3, row.get("email"));
                statement.addBatch(); // ← Agregar a lote en lugar de ejecutar inmediatamente
                recordCount++;
            }

            System.out.println("Procesando " + recordCount + " registros del CSV...");

            int[] results = statement.executeBatch(); // ← Ejecutar todas las inserciones en un solo lote
            conn.commit(); // ← Confirmar transacción

            System.out.println("Datos de clientes insertados exitosamente. Filas afectadas: " + results.length);

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error de integridad: La tabla Cliente ya contiene estos datos");
            safeRollback();
        } catch (IOException | SQLException e) {
            System.err.println("Error al insertar clientes: " + e.getMessage());
            safeRollback();
            throw new RuntimeException("Error al insertar clientes", e);
        } catch (NumberFormatException e) {
            System.err.println("Error de formato numérico en el CSV: " + e.getMessage());
            safeRollback();
            throw new RuntimeException("Error en formato de datos del CSV", e);
        } finally {
            safeRestoreAutoCommit();
        }
    }

    @Override
    public List<ClienteFactura> getListClientByBilling() {
        String query = "SELECT c.nombre, c.email, COUNT(f.idFactura) as cantidad_facturas " +
                "FROM Cliente c " +
                "JOIN facturas f ON c.idCliente = f.idCliente " +
                "GROUP BY c.idCliente, c.nombre, c.email " +
                "ORDER BY COUNT(f.idFactura) DESC";

        List<ClienteFactura> resultados = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultados.add(new ClienteFactura(
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getInt("cantidad_facturas")
                ));
            }

            System.out.println("Encontrados " + resultados.size() + " clientes con facturas");
            return resultados;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener listado de clientes", e);
        }
    }

    // Métodos auxiliares para manejo seguro de transacciones
    private void safeRollback() {
        try {
            if (!conn.getAutoCommit()) {
                conn.rollback();
                System.out.println("Transacción revertida exitosamente");
            }
        } catch (SQLException ex) {
            System.err.println("Error al hacer rollback: " + ex.getMessage());
        }
    }

    private void safeRestoreAutoCommit() {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Error al restaurar auto-commit: " + e.getMessage());
        }
    }

    // Método adicional para debug de la estructura del CSV
    public void debugCSVContents() {
        File csvFile = new File(csvPath);
        System.out.println("=== DEBUG CSV ===");
        System.out.println("Ruta: " + csvFile.getAbsolutePath());
        System.out.println("Existe: " + csvFile.exists());

        if (csvFile.exists()) {
            try (FileReader reader = new FileReader(csvFile);
                 CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader)) {

                System.out.println("Headers: " + String.join(", ", parser.getHeaderMap().keySet()));

                int sampleCount = 0;
                for (CSVRecord record : parser) {
                    if (sampleCount < 3) { // Mostrar solo 3 registros de muestra
                        System.out.println("Registro " + (sampleCount + 1) + ": " + record.toString());
                    }
                    sampleCount++;
                }
                System.out.println("Total registros en CSV: " + sampleCount);

            } catch (IOException e) {
                System.err.println("Error al leer CSV para debug: " + e.getMessage());
            }
        }
        System.out.println("=== FIN DEBUG ===");
    }
}