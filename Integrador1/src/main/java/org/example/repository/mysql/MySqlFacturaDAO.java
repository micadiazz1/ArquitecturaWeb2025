package org.example.repository.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.FacturaDAO;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

public class MySqlFacturaDAO implements FacturaDAO {

    private  final Connection connection;

    public MySqlFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTable() {
        String createFacturaTable = "CREATE TABLE Factura (" +
                "idFactura INT PRIMARY KEY AUTO_INCREMENT," +
                "idCliente INT," +
                "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)" +
                ")";
        try(PreparedStatement stmt = connection.prepareStatement(createFacturaTable)) {
            stmt.executeUpdate();
        } catch (SQLSyntaxErrorException e){
            System.out.println("Ya existe la tabla Factura");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void insertFactura() {

        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/java/org.example/utils/facturas.csv"));


            String insert = "INSERT INTO Factura (idFactura,idCliente) VALUES (?,?)";
            try(PreparedStatement statement = connection.prepareStatement(insert)) {
                for(CSVRecord row: parser) {

                    statement.setString(1,row.get("idFactura"));
                    statement.setString(2,row.get("idCliente"));

                    statement.executeUpdate();

                }
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("La tabla Factura ya esta cargada");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
