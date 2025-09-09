package org.example.repository.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.ClienteDAO;
import org.example.entity.ClienteFactura;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad {@code Cliente} bajo JDBC con MySQL.
 * <p>
 * Funcionalidades principales:
 * <ul>
 *   <li>Crea la tabla {@code Cliente} en la base de datos</li>
 *   <li>Inserta registros desde un archivo CSV</li>
 *   <li>Devuelve un listado de clientes ordenados por facturación</li>
 * </ul>
 */
public class MySqlClienteDAO implements ClienteDAO {

    /** Conexión activa a la base de datos MySQL */
    private final Connection conn;
    private static volatile MySqlClienteDAO instance;

    /** Ruta al archivo CSV con datos de clientes
     *  Esto se debe arreglar, el CSVReader por alguna razon no reconoce el path.
     * */
    private final String csvPath = "src/main/java/org/example/utils/clientes.csv";

    /**
     * Constructor de clase.
     *
     * @param conn conexión con la base de datos
     */
    private MySqlClienteDAO(Connection conn) {
        this.conn = conn;
    }

    public static MySqlClienteDAO getInstance(Connection conn) {
        if(instance == null){
            return new MySqlClienteDAO(conn);
        }
        return instance;
    }

    /**
     * Crea la tabla {@code Cliente} en la base de datos si no existe.
     *
     * @throws RuntimeException si ocurre un error SQL al crear la tabla
     */
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

    /**
     * Inserta registros en la tabla {@code Cliente}, leyendo datos desde un archivo CSV.
     * <p>
     * Utiliza transacciones y ejecución en batch para optimizar el proceso.
     *
     * @throws RuntimeException si ocurre un error de SQL, I/O o formato de datos
     */
    @Override
    public void insertCliente(CSVParser elementos) {
        File csvFile = new File(csvPath);
        System.out.println("Buscando archivo CSV en: " + csvFile.getAbsolutePath());
        System.out.println("El archivo existe: " + csvFile.exists());
        System.out.println("Es readable: " + csvFile.canRead());



        try (
                CSVParser parser = ManagerCSV.getInstance().getRecords(csvPath);
                PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Cliente (idCliente, nombre, email) VALUES (?, ?, ?)")) {

            conn.setAutoCommit(false);

            int recordCount = 0;
            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idCliente")));
                statement.setString(2, row.get("nombre"));
                statement.setString(3, row.get("email"));
                statement.addBatch();
                recordCount++;
            }

            System.out.println("Procesando " + recordCount + " registros del CSV...");
            int[] results = statement.executeBatch();
            conn.commit();

            System.out.println("Datos insertados exitosamente. Filas afectadas: " + results.length);

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

    /**
     * Devuelve una lista de clientes ordenada en forma descendente
     * según la cantidad de facturas asociadas.
     *
     * @return lista de clientes con nombre, email y cantidad de facturas
     * @throws RuntimeException si ocurre un error SQL durante la consulta
     */
    @Override
    public List<ClienteFactura> getListClientByBilling() {
        String query = "SELECT c.nombre, c.email, COUNT(f.idFactura) as cantidad_facturas " +
                "FROM cliente c " +
                "JOIN factura f ON c.idCliente = f.idCliente " +
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

    /**
     * Realiza un rollback seguro de la transacción activa si está habilitada.
     */
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

    /**
     * Restaura el modo auto-commit de la conexión.
     */
    private void safeRestoreAutoCommit() {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Error al restaurar auto-commit: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para debug:
     * muestra información del archivo CSV, headers y algunos registros de muestra.
     */
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
                    if (sampleCount < 3) {
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
