package org.example.integrador3.service.dto.inscripcion.response;

import org.example.integrador3.domain.Carrera;
import org.example.integrador3.domain.Estudiante;
import org.example.integrador3.domain.Inscripcion;

import java.sql.Timestamp;

public record InscripcionResponseDTO(
        Estudiante estudiante,
        Carrera carrera,
        Timestamp fechaInscripcion,
        Timestamp fechaGraduacion,
        int antiguedad
) {
    public InscripcionResponseDTO(Inscripcion i){
        this(i.getEstudiante(), i.getCarrera(), i.getFechaInscripcion(), i.getFechaGraduacion(), i.getAntiguedad());
    }


}
