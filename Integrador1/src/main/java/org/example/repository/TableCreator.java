package org.example.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

        private final Connection connection;

        public TableCreator(Connection connection) {
            this.connection = connection;
        }

        public void createAllTables() {
            System.out.println("Creando esquema de la base de datos...");

            try (Statement stmt = connection.createStatement()) {

                // Creación de la tabla cliente
                String createClienteTable = "CREATE TABLE IF NOT EXISTS cliente (" +
                        "idCliente INT PRIMARY KEY," +
                        "nombre VARCHAR(50)," +
                        "email VARCHAR(64)" +
                        ")";
                stmt.executeUpdate(createClienteTable);

                // Creación de la tabla producto
                String createProductoTable = "CREATE TABLE IF NOT EXISTS producto (" +
                        "idProducto INT PRIMARY KEY," +
                        "nombre VARCHAR(50)," +
                        "valor FLOAT" +
                        ")";
                stmt.executeUpdate(createProductoTable);

                // Creación de la tabla factura
                String createFacturaTable = "CREATE TABLE IF NOT EXISTS factura (" +
                        "idFactura INT PRIMARY KEY," +
                        "idCliente INT," +
                        "FOREIGN KEY (idCliente) REFERENCES cliente(idCliente)" +
                        ")";
                stmt.executeUpdate(createFacturaTable);

                // Creación de la tabla factura_producto
                String createFacturaProductoTable = "CREATE TABLE IF NOT EXISTS factura_producto (" +
                        "idFactura INT," +
                        "idProducto INT," +
                        "cantidad INT," +
                        "PRIMARY KEY (idFactura, idProducto)," +
                        "FOREIGN KEY (idFactura) REFERENCES factura(idFactura)," +
                        "FOREIGN KEY (idProducto) REFERENCES producto(idProducto)" +
                        ")";
                stmt.executeUpdate(createFacturaProductoTable);

                System.out.println("Esquema creado con éxito.");

            } catch (SQLException e) {
                System.err.println("Error al crear el esquema de la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

