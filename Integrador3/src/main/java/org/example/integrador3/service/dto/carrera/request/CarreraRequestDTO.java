package org.example.integrador3.service.dto.carrera.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.example.integrador3.domain.Inscripcion;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CarreraRequestDTO {
    private int id;
    private String nombre;
    private int duracion;
    private Set<Inscripcion> inscripciones;
}
