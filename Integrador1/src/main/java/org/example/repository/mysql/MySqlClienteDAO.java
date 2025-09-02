package org.example.repository.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.ClienteDAO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class MySqlClienteDAO implements ClienteDAO {
    private final Connection conn;

    public MySqlClienteDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createTable()  {
        String create ="CREATE TABLE Cliente (" +
                "idCliente INT PRIMARY KEY AUTO_INCREMENT," +
                "nombre VARCHAR(500)," +
                "email VARCHAR(150)" +
                ")";
        try (PreparedStatement statement = conn.prepareStatement(create)){
               statement.executeUpdate();
        }catch (SQLSyntaxErrorException e) {
            System.out.println("Ya existe la tabla Cliente");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void insertCliente()  {
        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/java/org.example/utils/clientes.csv"));
            String insert = "INSERT INTO Cliente (idCliente,nombre,email) VALUES (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            for (CSVRecord row : parser) {
                statement.setString(1, row.get("idCliente"));
                statement.setString(2, row.get("nombre"));
                statement.setString(3, row.get("email"));
                statement.executeUpdate();
            }

        }catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("La tabla Cliente ya esta cargada");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getListClientByBilling() {

    }
}
