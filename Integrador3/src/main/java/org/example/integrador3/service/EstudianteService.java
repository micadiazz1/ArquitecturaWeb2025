package org.example.integrador3.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.integrador3.domain.Estudiante;
import org.example.integrador3.domain.utils.Genero;
import org.example.integrador3.repository.EstudianteRepository;
import org.example.integrador3.service.dto.estudiante.request.EstudianteRequestDTO;
import org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;


    /**
     * a) dar de alta a un estudiante
     * */

    public EstudianteResponseDTO save(EstudianteRequestDTO req) { //DTO especifico para requests con todos los params
        final var estudiante  = new Estudiante(req);
        final var result= estudianteRepository.save(estudiante);
        return new EstudianteResponseDTO(estudiante);
    }

    /**
     * Recuperar todos los estudiantes
     * */
    public List<EstudianteResponseDTO> findAll(){
        return this.estudianteRepository.findAll().stream().map(EstudianteResponseDTO::new).toList();
    }

    /**
     * c) Recuperar todos los estudiantes, y especificar algún criterio de ordenamiento simple
     * */
    public List<EstudianteResponseDTO> findAllOrderedByApellidoAndNombre(){
        return estudianteRepository.findAllOrderedByApellidoAndNombre();
    }

    /**
     * d) recuperar un estudiante, en base a su número de libreta universitaria
     * */
    public EstudianteResponseDTO findByLibreta(int numLibreta){
        return this.estudianteRepository.findByNumLibreta(numLibreta);
    }

    /**
     * e) recuperar todos los estudiantes, en base a su género.
     * */
    public List<EstudianteResponseDTO> findByGenero(String genero){
        return this.estudianteRepository.findByGenero(genero);
    }

    public EstudianteResponseDTO getEstudianteByNumLibreta(Integer numLibreta) {
        return this.estudianteRepository.findByNumLibreta(numLibreta);
    }
}
