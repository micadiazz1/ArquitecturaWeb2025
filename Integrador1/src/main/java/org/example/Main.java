package org.example;

import org.example.entity.ClienteFactura;
import org.example.entity.TopProducto;
import org.example.repository.*;
import org.example.DAO.*;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    private static final MySqlDAOFactory DAOFactoryMySQL = new MySqlDAOFactory();
    private static final ClienteDAO clienteDAO = DAOFactoryMySQL.getClienteDAO();
    private static final FacturaDAO facturaDAO = DAOFactoryMySQL.getFacturaDAO();
    private static final FacturaProductoDAO facturaProductoDAO = DAOFactoryMySQL.getFacturaProductoDAO();
    private static final ProductoDAO productoDAO = DAOFactoryMySQL.getProductDAO();

    public static void main(String[] args) {
        //crear tablas
//        crearTablas();

        //llenar tablas con datos
        //falta arreglar el tema de que no reconoce las rutas de los archivos .csv
        llenarTablas();

        //Obtener el top producto
        TopProducto topProducto = productoDAO.getTopProduct();
        System.out.println(topProducto);

        //Obtener lista de clientes segun recaudación
        List<ClienteFactura> clientes = clienteDAO.getListClientByBilling();
        for (ClienteFactura cliente : clientes) {
            System.out.println(cliente);
        }
    }
    public static void crearTablas(){
        clienteDAO.createTable();
        facturaDAO.createTable();
        productoDAO.createTable();
        facturaProductoDAO.createTable();
    }

    public static void llenarTablas(){
        try {
            clienteDAO.insertCliente();

        } catch (FileNotFoundException e) {
            System.out.println("Error al insertar los clientes en tabla");
            throw new RuntimeException(e);
        }

        try {
            productoDAO.insertProducto();
        } catch (Exception e){
            System.out.println("Error al insertar los productos en tabla");
            throw new RuntimeException(e);
        }

        try {
            facturaDAO.insertFactura();
        } catch (Exception e){
            System.out.println("Error al insertar las facturas  en tabla");
            throw new RuntimeException(e);
        }

        try {
            facturaProductoDAO.insertFacturaProducto();
        } catch (Exception e){
            System.out.println("Error al insertar las facturas  en tabla");
            throw new RuntimeException(e);
        }
    }
}