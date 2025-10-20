package org.example.integrador3.service.dto.estudiante.response;

import org.example.integrador3.domain.Estudiante;

public record EstudianteResponseDTO(String nombre, String apellido, long dni, int numLibreta){
    public EstudianteResponseDTO(Estudiante e) {
        this(e.getNombre(), e.getApellido(), e.getDocumento(), e.getNumLibreta());
    }
}
