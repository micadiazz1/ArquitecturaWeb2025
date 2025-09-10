package org.example.factory;

import org.example.DAO.ClienteDAO;
import org.example.DAO.FacturaDAO;
import org.example.DAO.FacturaProductoDAO;
import org.example.DAO.ProductoDAO;
import org.example.repository.MySqlDAOFactory;


public abstract class DAOFactory {
    private static final int mysql = 1;
    private static final int derby = 2; //no implementada
    private static DAOFactory instance;
    public static DAOFactory getInstance(int db){
        if(instance == null) {
            synchronized (DAOFactory.class) {
                if (instance == null) {
                    switch (db) {
                        case mysql:
                            instance = new MySqlDAOFactory();
                            break;
                        default:
                            return null;
                    }
                }
            }
        }
        return instance;


    }
    

    // Métodos abstractos que deben ser implementados por las fábricas concretas
    public abstract ProductoDAO getProductDAO();
    public abstract ClienteDAO getClienteDAO();
    public abstract FacturaProductoDAO getFacturaProductoDAO();
    public abstract FacturaDAO getFacturaDAO();
}




