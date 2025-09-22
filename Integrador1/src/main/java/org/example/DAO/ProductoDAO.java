package org.example.DAO;

import org.example.entity.Producto;
import org.example.entity.TopProducto;

import java.util.List;

public interface ProductoDAO {

    void insertAll(List<Producto> productos);

    TopProducto getTopProduct();

}
