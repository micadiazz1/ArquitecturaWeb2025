package org.example.DTO;

public class CarreraDTO {
    private String nombre;
    private int duracion;
    long cantInscriptos;
    public CarreraDTO(String nombre, int duracion, int cantInscriptos) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.cantInscriptos = cantInscriptos;
    }

}
