package org.example.entity;

public class Factura {
    private int idFactura;
    private int idCliente;

    public Factura(int idFactura, int idCliente) {
        this.idFactura = idFactura;
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "idFactura=" + idFactura +
                ", idCliente=" + idCliente +
                '}';
    }

    public int getIdFactura() {
        return idFactura;
    }

    public int getIdCliente() {
        return idCliente;
    }
}
