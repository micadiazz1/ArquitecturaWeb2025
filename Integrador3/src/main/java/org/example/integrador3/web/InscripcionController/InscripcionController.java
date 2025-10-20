package org.example.integrador3.web.InscripcionController;


import lombok.RequiredArgsConstructor;
import org.example.integrador3.service.dto.carrera.response.CarreraResponseDTO;
import org.example.integrador3.service.dto.inscripcion.request.InscripcionRequestDTO;
import org.example.integrador3.service.dto.estudiante.response.EstudianteResponseDTO;
import org.example.integrador3.service.dto.inscripcion.response.InscripcionResponseDTO;
import org.example.integrador3.service.dto.reporte.CarreraReporteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.integrador3.service.InscripcionService;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;


    @GetMapping
    public ResponseEntity<List<InscripcionResponseDTO>> findAll() {
        return ResponseEntity.ok(this.inscripcionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscripcionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.inscripcionService.findInscripcionById(id));
    }

    @PostMapping
    public ResponseEntity<InscripcionResponseDTO> save(@RequestBody InscripcionRequestDTO inscripcion) {
        return ResponseEntity.ok(this.inscripcionService.save(inscripcion));
    }

    //Path específico para evitar duplicados
    @GetMapping("/estudiantes/{carrera}")
    public ResponseEntity<List<EstudianteResponseDTO>> findEstudiantesByCarrera(
            @RequestParam Long carreraId,
            @RequestParam(required = false) String ciudad) {
        return ResponseEntity.ok(this.inscripcionService.findEstudiantesByCarrera(carreraId, ciudad));
    }

    //Path específico
    @GetMapping("/carreras/inscriptos")
    public ResponseEntity<List<CarreraResponseDTO>> findCarrerasByInscriptos() {
        return ResponseEntity.ok(this.inscripcionService.findCarrerasByInscriptos());
    }

    @GetMapping("/carreras/reporte")
    public ResponseEntity<List<CarreraReporteDTO>> generarReporteCarreras() {
        return ResponseEntity.ok(this.inscripcionService.generarReporteCarreras());
    }
}