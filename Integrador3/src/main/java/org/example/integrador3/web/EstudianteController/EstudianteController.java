package org.example.integrador3.web.EstudianteController;


import lombok.RequiredArgsConstructor;
import org.example.integrador3.domain.utils.Genero;
import org.example.integrador3.service.EstudianteService;
import org.example.integrador3.service.dto.estudiante.request.EstudianteRequestDTO;
import org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {
    private final EstudianteService estudianteService;

    @GetMapping
    public List<EstudianteResponseDTO> getEstudiantes(){
        return this.estudianteService.findAll();
    }

    @GetMapping("/ordered")
    public List<EstudianteResponseDTO> getOrderedEstudiantes(){
        return this.estudianteService.findAllOrderedByApellidoAndNombre();
    }
    //No es necesario mandarle ("")
    //La ruta sigue siendo /api/estudiantes
    @PostMapping
    public ResponseEntity<EstudianteResponseDTO>saveEstudiante(@RequestBody EstudianteRequestDTO estudiante){
        final var result=this.estudianteService.save(estudiante);
        return ResponseEntity.accepted().body(result);
    }

    //obtener x libreta
    @GetMapping("/libreta/{numLibreta}")
    public EstudianteResponseDTO getEstudianteByNumLibreta(@PathVariable Integer numLibreta){
        return this.estudianteService.getEstudianteByNumLibreta(numLibreta);
    }
    @GetMapping("/genero/{genero}")
    public List<EstudianteResponseDTO> getEstudiantesByGenero(@PathVariable String genero){
        return this.estudianteService.findByGenero(genero);
    }




}
