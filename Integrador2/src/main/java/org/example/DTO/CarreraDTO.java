package org.example.DTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CarreraDTO {
    private String nombre;
    private int duracion;
    private long cantInscriptos;

    public CarreraDTO(String nombre, int duracion, long cantInscriptos) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.cantInscriptos = cantInscriptos;
    }
}

