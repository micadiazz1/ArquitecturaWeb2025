package org.example.integrador3.repository;

import org.example.integrador3.domain.Carrera;
import org.example.integrador3.domain.Inscripcion;
import org.example.integrador3.service.dto.carrera.response.CarreraResponseDTO;
import org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    // f) carreras con estudiantes inscriptos, ordenadas por cantidad de inscriptos
    @Query("""
           SELECT c 
           FROM Inscripcion i 
           JOIN i.carrera c 
           GROUP BY c 
           ORDER BY COUNT(i) DESC
           """)
    List<CarreraResponseDTO> findCarrerasByInscriptos();

    // g) estudiantes por carrera y ciudad
    @Query("""
           SELECT DISTINCT new org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO(
                e.nombre, e.apellido, e.documento, e.numLibreta
           )
           FROM Inscripcion i 
           JOIN i.estudiante e 
           WHERE i.carrera.id = :idCarrera AND e.ciudad = :ciudad
           """)
    List<EstudianteResponseDTO> findEstudiantesByCarrera(
            @Param("idCarrera") Long idCarrera,
            @Param("ciudad") String ciudad
    );
}
