package org.example.DAO;

import org.example.DTO.ReporteCarrera;
import org.example.entities.Carrera;
import org.example.entities.Ciudad;
import org.example.entities.Estudiante;
import org.example.entities.Inscripcion;

import java.util.List;

public interface InscripcionDAO {

    void matricularEstudiante(Inscripcion insc);
    List<Carrera> getCarrerasConInscriptos();
    List<Estudiante>  getEstudiantesByCarrera(Carrera carrera, Ciudad ciudad);

    /**
     * TODO - <strong>Terminar la funcion del reporte de carreras </strong>
     * */
    List<ReporteCarrera> generarReporteCarreras();
}
