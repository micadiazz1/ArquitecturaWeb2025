package org.example.repository.mysql;

import org.example.DAO.InscripcionDAO;
import org.example.DTO.ReporteCarrera;
import org.example.entities.Carrera;
import org.example.entities.Ciudad;
import org.example.entities.Estudiante;
import org.example.entities.Inscripcion;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.List;

public class MySqlInscripcionDAO implements InscripcionDAO {

    private static volatile MySqlInscripcionDAO instance;
    private EntityManager em;

    private MySqlInscripcionDAO(EntityManager em) {
        this.em = em;
    }

    public static MySqlInscripcionDAO getInstance(EntityManager em) {
        return instance == null ? instance = new MySqlInscripcionDAO(em) : instance;
    }

    /**
     * b) matricular un estudiante en una carrera
     * */
    @Override
    public void matricularEstudiante(Inscripcion insc) {
        em.persist(insc);
    }

    /**
     * g) recuperar los estudiantes de una determinada carrera, filtrado por ciudad de residencia
     * */

    @Override
    public List<Estudiante> getEstudiantesByCarrera(Carrera carrera, Ciudad ciudad) {
        String query = "SELECT DISTINCT i.estudiante FROM Inscripcion i" +
                " WHERE i.carrera = :carrera" +
                " AND i.estudiante.ciudad = :ciudad";

        return em.createQuery(query, Estudiante.class)
                .setParameter("carrera", carrera)
                .setParameter("ciudad", ciudad)
                .getResultList();
    }

    /**
     *
     * <li>Query:
     *      <strong></br>
     *           SELECT c, COUNT(i) AS total  </br>
     *           FROM Inscripcion i </br>
     *           JOIN i.carrera c </br>
     *           GROUP BY c </br>
     *           ORDER BY total DESC </br>
     *      </strong>
     * </li>

     *
     * <li> Recuperar los estudiantes de una determinada carrera, filtrado por ciudad de residencia </li>
     * */

    @Override
    public List<Carrera> getCarrerasConInscriptos() {
        String query = "SELECT c, COUNT(i) AS total" +
                " FROM Inscripcion i" +
                " JOIN i.carrera c" +
                " GROUP BY c" +
                " ORDER BY total DESC";

        return em.createQuery(query, Carrera.class).getResultList();
    }

    /**
     * 3) Generar un reporte de las carreras, que para cada carrera incluya información de los
     * inscriptos y egresados por año. Se deben ordenar las carreras alfabéticamente, y presentar
     * los años de manera cronológica.
     *
     * <p>
     *  <li> Esto te da, por carrera y año, la cantidad de inscriptos:
     *      <strong> </br>
     *      SELECT c.nombreCarrera, YEAR(i.fechaInscripcion), COUNT(i.estudiante) </br>
     *      FROM Inscripcion i </br>
     *      JOIN i.carrera c </br>
     *      GROUP BY c.nombreCarrera, YEAR(i.fechaInscripcion) </br>
     *      ORDER BY c.nombreCarrera ASC, YEAR(i.fechaInscripcion) ASC </br>
     *      </strong>
     *  </li>
     *
     *
     *
     *
     *
     *  <li>Trae los graduados por carrera y año:
     *      <strong> </br>
     *          SELECT c.nombreCarrera, YEAR(e.fechaGraduacion), COUNT(e) </br>
     *          FROM Estudiante e </br>
     *          JOIN e.inscripciones i </br>
     *          JOIN i.carrera c </br>
     *          WHERE e.graduado = true </br>
     *          GROUP BY c.nombreCarrera, YEAR(e.fechaGraduacion) </br>
     *          ORDER BY c.nombreCarrera ASC, YEAR(e.fechaGraduacion) ASC </br>
     *      </strong>
     *  </li>
     *
     * </p>
     *
     * TODO - Terminar la funcion del reporte de carreras
     *
     * */

    @Override
    public List<ReporteCarrera> generarReporteCarreras() {
        return List.of();
    }
}
