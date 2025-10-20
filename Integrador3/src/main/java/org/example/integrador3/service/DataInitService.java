package org.example.integrador3.service;


import org.example.integrador3.domain.Carrera;
import org.example.integrador3.domain.Estudiante;
import org.example.integrador3.domain.Inscripcion;
import org.example.integrador3.domain.utils.Genero;
import org.example.integrador3.repository.CarreraRepository;
import org.example.integrador3.repository.EstudianteRepository;
import org.example.integrador3.repository.InscripcionRepository;
import org.example.integrador3.utils.CSVLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DataInitService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Transactional
    public void loadEstudiantes() {
        try {
            CSVLoader<Estudiante> estudianteLoader = new CSVLoader<>(estudianteRepository);
            estudianteLoader.loadAndInsert("estudiantes.csv", record -> {
                Estudiante estudiante = new Estudiante();
                estudiante.setDocumento(Integer.parseInt(record.get("DNI")));
                estudiante.setNombre(record.get("nombre"));
                estudiante.setApellido(record.get("apellido"));
                estudiante.setEdad(Integer.parseInt(record.get("edad")));
                estudiante.setGenero(String.valueOf(record.get("genero").toLowerCase()));
                estudiante.setCiudad(record.get("ciudad"));
                estudiante.setNumLibreta(Integer.parseInt(record.get("LU")));
                return estudiante;
            });
        } catch (IOException e) {
            throw new RuntimeException("Error de lectura en estudiantes.csv", e);
        }
    }

    @Transactional
    public void loadCarreras() {
        try {
            CSVLoader<Carrera> carreraLoader = new CSVLoader<>(carreraRepository);
            carreraLoader.loadAndInsert("carreras.csv", record -> {
                Carrera carrera = new Carrera();
                carrera.setId(Integer.parseInt(record.get("id_carrera")));
                carrera.setNombre(record.get("carrera"));
                carrera.setDuracion(Integer.parseInt(record.get("duracion")));
                return carrera;
            });
        } catch (IOException e) {
            throw new RuntimeException("Error de lectura en carreras.csv", e);
        }
    }

    @Transactional
    public void loadInscripciones() {
        try {
            CSVLoader<Inscripcion> inscripcionLoader = new CSVLoader<>(inscripcionRepository);
            inscripcionLoader.loadAndInsert("estudianteCarrera.csv", record -> {
                int documentoEstudiante = Integer.parseInt(record.get("id_estudiante"));
                int idCarreraCsv = Integer.parseInt(record.get("id_carrera"));

                Date inscripcionDate = null;
                try {
                    inscripcionDate = new SimpleDateFormat("yyyy").parse(record.get("inscripcion"));
                } catch (ParseException e) {
                    throw new RuntimeException("Error al parsear fecha de inscripción", e);
                }

                Date graduacionDate = null;
                String graduacionStr = record.get("graduacion");
                if (graduacionStr != null && !graduacionStr.isEmpty()) {
                    try {
                        graduacionDate = new SimpleDateFormat("yyyy").parse(graduacionStr);
                    } catch (ParseException e) {
                        // La fecha se deja como null si no se puede parsear
                    }
                }

                int antiguedad = Integer.parseInt(record.get("antiguedad"));

                Estudiante estudiante = estudianteRepository.findById((long) documentoEstudiante)
                        .orElseThrow(() -> new RuntimeException("Estudiante con DNI " + documentoEstudiante + " no encontrado"));
                Carrera carrera = carreraRepository.findById(idCarreraCsv)
                        .orElseThrow(() -> new RuntimeException("Carrera con ID " + idCarreraCsv + " no encontrada"));

                return new Inscripcion(
                        estudiante,
                        carrera,
                        new Timestamp(inscripcionDate.getTime()),
                        graduacionDate != null ? new Timestamp(graduacionDate.getTime()) : null,
                        antiguedad
                );
            });
        } catch (IOException e) {
            throw new RuntimeException("Error de lectura en estudianteCarrera.csv", e);
        }
    }

    @Transactional
    public void loadAll() {
        loadCarreras();
        loadEstudiantes();
        loadInscripciones();
    }
}