package org.example.repository.mysql;

import org.example.DAO.FacturaDAO;

import java.sql.Connection;

public class MySqlFacturaDAO implements FacturaDAO {

    private  final Connection connection;

    public MySqlFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTable() {

    }

    @Override
    public void insertFactura() {

    }
}
