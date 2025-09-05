package org.example.DAO;
import org.example.entity.Producto;
import org.example.entity.TopProducto;

public interface ProductoDAO {

    void createTable();

    void insertProducto();

    TopProducto getTopProduct();

}
