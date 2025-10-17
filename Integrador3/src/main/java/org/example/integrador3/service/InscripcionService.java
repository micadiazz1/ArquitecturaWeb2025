package org.example.integrador3.service;

import jakarta.transaction.Transactional;
import org.example.integrador3.domain.Carrera;
import org.example.integrador3.domain.Inscripcion;
import org.example.integrador3.repository.InscripcionRepository;
import org.example.integrador3.service.dto.carrera.response.CarreraResponseDTO;
import org.example.integrador3.service.dto.inscripcion.request.InscripcionRequestDTO;
import org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO;
import org.example.integrador3.service.dto.inscripcion.response.InscripcionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;

    @Autowired
    public InscripcionService(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    /**Devuelve todas las inscripciones como DTO */
    public List<InscripcionResponseDTO> findAll() {
        return this.inscripcionRepository.findAll()
                .stream()
                .map(InscripcionResponseDTO::new)
                .toList();
    }

    /** Devuelve una inscripción por ID como DTO */
    public InscripcionResponseDTO findInscripcionById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválida");
        }

        Inscripcion inscripcion = this.inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + id));

        return new InscripcionResponseDTO(inscripcion);
    }

    /**Guarda una inscripción validando sus datos
     *
     * b) matricular un estudiante en la carrera
     * */
    public InscripcionResponseDTO save(InscripcionRequestDTO inscripcion) {
        if (inscripcion == null) {
            throw new IllegalArgumentException("Inscripción inválida");
        }

        /*
         * Carrera
         * */
        if (inscripcion.getCarrera() == null) {
            throw new IllegalArgumentException("Carrera inválida");
        }

        /*
         * Estudiante
         * */
        if (inscripcion.getEstudiante() == null) {
            throw new IllegalArgumentException("Estudiante inválido");
        }

        if (inscripcion.getAntiguedad() < 0) {
            throw new IllegalArgumentException("Antigüedad inválida, debe ser mayor o igual a 0");
        }

        Inscripcion saved = this.inscripcionRepository.save(new Inscripcion(inscripcion));
        return new InscripcionResponseDTO(saved);
    }

    /** f) Carreras ordenadas por cantidad de inscriptos */
    public List<CarreraResponseDTO> findCarrerasByInscriptos() {
        return this.inscripcionRepository.findCarrerasByInscriptos();
    }

    /**g) Estudiantes por carrera y ciudad */
    public List<EstudianteResponseDTO> findEstudiantesByCarrera(Long idCarrera, String ciudad) {
        if (idCarrera == null || idCarrera <= 0 || ciudad == null || ciudad.isBlank()) {
            throw new IllegalArgumentException("Carrera o ciudad inválida");
        }

        return this.inscripcionRepository.findEstudiantesByCarrera(idCarrera, ciudad);
    }
}
