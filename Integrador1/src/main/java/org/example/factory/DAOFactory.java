package org.example.factory;

import org.example.DAO.ClienteDAO;
import org.example.DAO.FacturaDAO;
import org.example.DAO.FacturaProductoDAO;
import org.example.DAO.ProductoDAO;


public abstract class DAOFactory {

    // Métodos abstractos que deben ser implementados por las fábricas concretas
    public abstract ProductoDAO getProductDAO();
    public abstract ClienteDAO getClienteDAO();
    public abstract FacturaProductoDAO getFacturaProductoDAO();
    public abstract FacturaDAO getFacturaDAO();
}




