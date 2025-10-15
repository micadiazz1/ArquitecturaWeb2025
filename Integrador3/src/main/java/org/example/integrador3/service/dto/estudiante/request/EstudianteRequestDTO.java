package org.example.integrador3.service.dto.estudiante.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EstudianteRequestDTO{
    private int numLibreta;
    private String nombre;
    private String apellido;
    private int edad;
    private int documento;
    private String genero;
    private String ciudad;

}
