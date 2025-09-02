package org.example.repository.mysql;

import org.example.DAO.ProductoDAO;

import java.sql.Connection;

public class MySqlProductoDAO implements ProductoDAO {
    private final Connection conn;

    public MySqlProductoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createTable() {

    }

    @Override
    public void insertProducto() {

    }
}
