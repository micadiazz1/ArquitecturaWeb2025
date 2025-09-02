package org.example.repository.mysql;

import org.example.DAO.ClienteDAO;

import java.sql.Connection;

public class MySqlClienteDAO implements ClienteDAO {
    private final Connection conn;

    public MySqlClienteDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createTable() {
        
    }

    @Override
    public void insertCliente() {

    }

    @Override
    public void getListClientByBilling() {

    }
}
