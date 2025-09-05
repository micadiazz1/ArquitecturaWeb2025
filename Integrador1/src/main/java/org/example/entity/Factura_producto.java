package org.example.entity;

public class Factura_producto {
    private int idFactura;
    private int idCliente;
    private int cantidad;

    public Factura_producto(int idFactura, int idCliente, int cantidad) {
        this.idFactura = idFactura;
        this.idCliente = idCliente;
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Factura_producto{" +
                "idFactura=" + idFactura +
                ", idCliente=" + idCliente +
                ", cantidad=" + cantidad +
                '}';
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
