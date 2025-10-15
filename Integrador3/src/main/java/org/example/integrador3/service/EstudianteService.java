package org.example.integrador3.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.integrador3.domain.Estudiante;
import org.example.integrador3.repository.EstudianteRepository;
import org.example.integrador3.service.dto.estudiante.request.EstudianteRequestDTO;
import org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;

    @Transactional
    public EstudianteResponseDTO save(EstudianteRequestDTO req) { //DTO especifico para requests con todos los params
        final var estudiante  = new Estudiante(req);
        final var result= estudianteRepository.save(estudiante);
        return new EstudianteResponseDTO(result.getNombre(), result.getApellido(), result.getDocumento(), result.getNumLibreta());
    }

    @Transactional
    public List<EstudianteResponseDTO> findAll(){
        return this.estudianteRepository.findAll().stream().map(EstudianteResponseDTO::new).toList();
    }
    public List<EstudianteResponseDTO> findAllOrderedByApellidoAndNombre(){
        return estudianteRepository.findAllOrderedByApellidoAndNombre();
    }




}
