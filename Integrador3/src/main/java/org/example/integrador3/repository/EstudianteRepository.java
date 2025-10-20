package org.example.integrador3.repository;

import org.example.integrador3.domain.Estudiante;
import org.example.integrador3.domain.utils.Genero;
import org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    @Query("SELECT new org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO(e.nombre, e.apellido, e.documento, e.numLibreta) " +
            "FROM Estudiante e WHERE e.numLibreta = :numLibreta")
    EstudianteResponseDTO findByNumLibreta(Integer numLibreta);

    @Query("SELECT new org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO(e.nombre, e.apellido, e.documento, e.numLibreta) " +
            "FROM Estudiante e WHERE e.genero = :genero")
    List<EstudianteResponseDTO> findByGenero(Genero genero);

    @Query("SELECT new org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO(e.nombre, e.apellido, e.documento, e.numLibreta) " +
            "FROM Estudiante e ORDER BY e.apellido ASC, e.nombre ASC")
    List<EstudianteResponseDTO> findAllOrderedByApellidoAndNombre();

    @Query("SELECT new org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO(e.id) " +
            "FROM Estudiante e WHERE e.idEstudiante = :idEstudiante")
    EstudianteResponseDTO findByIdEstudiante(
            @Param("idEstudiante") Long idEstudiante
    );
}
