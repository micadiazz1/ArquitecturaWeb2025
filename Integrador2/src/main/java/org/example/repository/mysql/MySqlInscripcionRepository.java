package org.example.repository.mysql;

import org.example.DTO.CarreraDTO;
import org.example.DTO.EstudianteDTO;
import org.example.DTO.ReporteCarrera;
import org.example.entities.Carrera;
import org.example.entities.Estudiante;
import org.example.entities.Inscripcion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class MySqlInscripcionRepository extends BaseJPARepository<Inscripcion>  {

    private static volatile MySqlInscripcionRepository instance;

    public MySqlInscripcionRepository(EntityManager em) {
        super(em, Inscripcion.class );
    }

    public static MySqlInscripcionRepository getInstance(EntityManager em) {
        return instance == null ? instance = new MySqlInscripcionRepository(em) : instance;
    }


    /* g) recuperar los estudiantes de una determinada carrera, filtrado por ciudad de residencia
     * */

    public List<EstudianteDTO> getEstudiantesByCarrera(Carrera carrera, String ciudad) {
        String query =
                "SELECT DISTINCT new EstudianteDTO(" +
                        "e.nombre, e.apellido, e.documento, e.numLibreta) " +
                        "FROM Inscripcion i " +
                        "JOIN i.estudiante e " +
                        "WHERE i.carrera = :carrera " +
                        "AND e.ciudad = :ciudad";

        return getEntityManager().createQuery(query, EstudianteDTO.class)
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


    public List<CarreraDTO> getCarrerasConInscriptos() {
        String query =
                "SELECT new CarreraDTO(c.nombre, c.duracion, COUNT(i)) " +
                        "FROM Inscripcion i " +
                        "JOIN i.carrera c " +
                        "GROUP BY c.nombre, c.duracion " +
                        "ORDER BY COUNT(i) DESC";

        return getEntityManager().createQuery(query, CarreraDTO.class).getResultList();

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


    public List<ReporteCarrera> generarReporteCarreras() {
        return List.of();
    }
}
