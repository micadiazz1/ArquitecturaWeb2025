package org.example.DAO;

import java.io.FileNotFoundException;

public interface ClienteDAO {

    void createTable();

    void insertCliente() throws FileNotFoundException;

    void getListClientByBilling();
}
