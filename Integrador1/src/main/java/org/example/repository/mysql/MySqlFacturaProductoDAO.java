package org.example.repository.mysql;

import org.example.DAO.FacturaProductoDAO;

import java.sql.Connection;

public class MySqlFacturaProductoDAO implements FacturaProductoDAO {
    private final Connection conn;

    public MySqlFacturaProductoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createTable() {

    }

    @Override
    public void insertFacturaProducto() {

    }
}
