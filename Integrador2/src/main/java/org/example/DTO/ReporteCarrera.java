package org.example.DTO;

import org.example.entities.Carrera;
import org.example.entities.Estudiante;

import java.util.List;

public record ReporteCarrera (Carrera carrera, List<Estudiante> infoInscriptos, List<Estudiante> egresadosPorAño, int anio){
}
