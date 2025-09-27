package org.example;

import org.example.DAO.ClienteDAO;
import org.example.DAO.FacturaDAO;
import org.example.DAO.FacturaProductoDAO;
import org.example.DAO.ProductoDAO;
import org.example.entity.*;

import org.example.factory.ConnectionManagerSingleton;
import org.example.factory.CsvLoader;
import org.example.factory.DAOFactory;
import org.example.repository.MySqlDAOFactory;
import org.example.repository.TableCreator;

import java.io.IOException;
import java.util.List;



public class Main {
    //obtener instancia de acuerdo a que base de datos se use, en este caso mySql(1)
    private static final  DAOFactory mysqlFactory = DAOFactory.getInstance(1);
    private static final ClienteDAO clienteDAO = mysqlFactory.getClienteDAO();
    private static final FacturaDAO facturaDAO = mysqlFactory.getFacturaDAO();
    private static final FacturaProductoDAO facturaProductoDAO = mysqlFactory.getFacturaProductoDAO();
    private static final ProductoDAO productoDAO = mysqlFactory.getProductDAO();

    public static void main(String[] args) {
        // 1. Crear las tablas
        crearTablas();

        // 2. Llenar tablas con datos
        llenarTablas();

        // 3. Obtener el top producto
        TopProducto topProducto = productoDAO.getTopProduct();
        System.out.println("\n--- Top producto ---");
        System.out.println(topProducto);

        // 4. Obtener lista de clientes según recaudación
        List<ClienteFactura> clientes = clienteDAO.getClientsSortedByTotalBilling();
        System.out.println("\n--- Clientes ordenados por facturación ---");
        for (ClienteFactura cliente : clientes) {
            System.out.println(cliente);
        }

        // Cierra la conexión al final del programa
        ConnectionManagerSingleton.getInstance().closeConnection();
    }

    public static void crearTablas() {
        System.out.println("Creando esquema de la base de datos...");
        TableCreator tableCreator = new TableCreator(ConnectionManagerSingleton.getInstance().getConnection());
        tableCreator.createAllTables();
    }

    public static void llenarTablas() {
        CsvLoader<Cliente> clienteLoader = new CsvLoader<>();
        CsvLoader<Producto> productoLoader = new CsvLoader<>();
        CsvLoader<Factura> facturaLoader = new CsvLoader<>();
        CsvLoader<FacturaProducto> facturaProductoLoader = new CsvLoader<>();

        try {
            // Cargar clientes
            clienteLoader.loadAndInsert("clientes.csv", clienteDAO, record -> new Cliente(
                    Integer.parseInt(record.get("idCliente")),
                    record.get("nombre"),
                    record.get("email")
            ));

            // Cargar productos
            productoLoader.loadAndInsert("productos.csv", productoDAO, record -> new Producto(
                    Integer.parseInt(record.get("idProducto")),
                    record.get("nombre"),
                    Float.parseFloat(record.get("valor"))
            ));

            // Cargar facturas
            facturaLoader.loadAndInsert("facturas.csv", facturaDAO, record -> new Factura(
                    Integer.parseInt(record.get("idFactura")),
                    Integer.parseInt(record.get("idCliente"))
            ));

            // Cargar factura_producto
            facturaProductoLoader.loadAndInsert("facturasProductos.csv", facturaProductoDAO, record -> new FacturaProducto(
                    Integer.parseInt(record.get("idFactura")),
                    Integer.parseInt(record.get("idProducto")),
                    Integer.parseInt(record.get("cantidad"))
            ));

        } catch (IOException e) {
            System.err.println("Error al cargar los archivos CSV: " + e.getMessage());
        }
    }
}


