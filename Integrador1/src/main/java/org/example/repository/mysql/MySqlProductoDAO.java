package org.example.repository.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.ProductoDAO;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class MySqlProductoDAO implements ProductoDAO {
    private final Connection conn;

    public MySqlProductoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createTable() {
        String createProductoTable = "CREATE TABLE Producto (" +
                "idProducto INT PRIMARY KEY AUTO_INCREMENT," +
                "nombre VARCHAR(45)," +
                "valor FLOAT" +
                ")";
        try(PreparedStatement statement = conn.prepareStatement(createProductoTable)) {
            statement.executeUpdate();
        } catch (SQLSyntaxErrorException e){
            System.out.println("Ya existe la tabla Producto");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void insertProducto() {
        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/java/org.example/utils/productos.csv"));

            String insert = "INSERT INTO Producto (idProducto,nombre,valor) VALUES (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insert);

            for (CSVRecord row : parser) {

                statement.setString(1, row.get("idProducto"));
                statement.setString(2, row.get("nombre"));
                statement.setString(3, row.get("valor"));
                statement.executeUpdate();

            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("La tabla Producto ya esta cargada");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getTopProduct() {

    }
}
