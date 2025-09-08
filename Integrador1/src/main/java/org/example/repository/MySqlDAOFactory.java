package org.example.repository;

import org.example.DAO.ClienteDAO;
import org.example.DAO.FacturaDAO;
import org.example.DAO.FacturaProductoDAO;
import org.example.DAO.ProductoDAO;
import org.example.factory.ConnectionManagerSingleton;
import org.example.factory.DAOFactory;
import org.example.repository.mysql.MySqlClienteDAO;
import org.example.repository.mysql.MySqlFacturaDAO;
import org.example.repository.mysql.MySqlFacturaProductoDAO;
import org.example.repository.mysql.MySqlProductoDAO;

public class MySqlDAOFactory extends DAOFactory {


    @Override
    public ProductoDAO getProductDAO() {
        return MySqlProductoDAO.getInstance(ConnectionManagerSingleton.getInstance().getConnection());
    }

    @Override
    public ClienteDAO getClienteDAO() {
        return MySqlClienteDAO.getInstance(ConnectionManagerSingleton.getInstance().getConnection());
    }

    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return MySqlFacturaProductoDAO.getInstance(ConnectionManagerSingleton.getInstance().getConnection());
    }

    @Override
    public FacturaDAO getFacturaDAO() {
        return MySqlFacturaDAO.getInstance(ConnectionManagerSingleton.getInstance().getConnection());
    }
}
