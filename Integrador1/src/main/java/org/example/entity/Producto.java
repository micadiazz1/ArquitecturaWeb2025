package org.example.entity;

public class Producto {
    private int idProducto;
    private String nombre;
    private Float valor;

    public Producto(int idProducto, String nombre, Float valor) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.valor = valor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public Float getValor() {
        return valor;
    }

    public String getNombre() {
        return nombre;
    }
}
