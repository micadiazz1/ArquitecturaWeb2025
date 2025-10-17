package org.example.integrador3.service.dto.inscripcion.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.example.integrador3.domain.Carrera;
import org.example.integrador3.domain.Estudiante;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class InscripcionRequestDTO {
    private int id;
    private Estudiante estudiante;
    private Carrera carrera;
    private Timestamp fechaInscripcion;
    private Timestamp fechaGraduacion;
    private int antiguedad;
}
