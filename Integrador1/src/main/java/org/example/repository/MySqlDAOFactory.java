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
        return new MySqlProductoDAO(ConnectionManagerSingleton.getInstance().getConnection());
    }

    @Override
    public ClienteDAO getClienteDAO() {
        return new MySqlClienteDAO(ConnectionManagerSingleton.getInstance().getConnection());
    }

    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return new MySqlFacturaProductoDAO(ConnectionManagerSingleton.getInstance().getConnection());
    }

    @Override
    public FacturaDAO getFacturaDAO() {
        return new MySqlFacturaDAO(ConnectionManagerSingleton.getInstance().getConnection());
    }
}
