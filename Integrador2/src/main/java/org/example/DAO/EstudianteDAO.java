package org.example.DAO;

import org.example.entities.Carrera;
import org.example.entities.Ciudad;
import org.example.entities.Estudiante;
import org.example.utils.Genero;

import java.util.List;

public interface EstudianteDAO {

    void addEstudiante(Estudiante estudiante);
    List<Estudiante> getEstudiantes();
    Estudiante getEstudianteByLibreta(int numLibreta);
    List<Estudiante> getEstudiantesByGenero(Genero genero);
}
