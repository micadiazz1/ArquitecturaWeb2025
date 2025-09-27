package org.example.DAO;

import org.example.entity.Factura;

import java.util.List;

public interface FacturaDAO {

    void insertAll(List<Factura> facturas);
}
