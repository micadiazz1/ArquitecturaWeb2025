package org.example.DAO;

import org.example.entity.Cliente;
import org.example.entity.ClienteFactura;

import java.util.List;

import java.io.FileNotFoundException;

public interface ClienteDAO {

    void createTable();

    void insertCliente() throws FileNotFoundException;

    List<ClienteFactura> getListClientByBilling();
}
