package org.example.integrador3.service.dto.carrera.response;

import org.example.integrador3.domain.Inscripcion;
import org.example.integrador3.service.dto.carrera.request.CarreraRequestDTO;

import java.util.Set;

public record CarreraResponseDTO(
        int id,
        String nombre,
        int duracion,
        Set<Inscripcion>inscripciones
) {

    public CarreraResponseDTO(CarreraRequestDTO c){
        this(
            c.getId(),
            c.getNombre(),
            c.getDuracion(),
            c.getInscripciones()
        );
    }
}
