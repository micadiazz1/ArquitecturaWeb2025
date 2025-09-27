package org.example.repository.mysql;

import org.example.DTO.CarreraDTO;
import org.example.DTO.EstudianteDTO;
import org.example.DTO.ReporteCarreraDTO;
import org.example.entities.Carrera;
import org.example.entities.Estudiante;
import org.example.entities.Inscripcion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
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
                "SELECT DISTINCT new org.example.DTO.EstudianteDTO(" +
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
                "SELECT new org.example.DTO.CarreraDTO(c.nombre, c.duracion, COUNT(i)) " +
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
     * TODO - Terminar la funcion del reporte de carreras
     *
     * */

    public List<ReporteCarreraDTO> generarReporteCarreras() {

        String query =
                "SELECT new org.example.DTO.ReporteCarreraDTO(" +
                        "   c.nombre, " +
                        "   YEAR(i.fechaInscripcion), " +
                        "   COUNT(i), " +
                        "   SUM(CASE WHEN i.fechaGraduacion <> :fechaDefault THEN 1 ELSE 0 END)" +
                        ") " +
                        "FROM Inscripcion i " +
                        "JOIN i.carrera c " +
                        "GROUP BY c.nombre, YEAR(i.fechaInscripcion) " +
                        "ORDER BY c.nombre ASC, YEAR(i.fechaInscripcion) ASC";

        // Crear Date compatible
        Calendar cal = Calendar.getInstance();
        cal.set(1, Calendar.JANUARY, 1, 3, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date fechaDefault = cal.getTime();

        return getEntityManager()
                .createQuery(query, ReporteCarreraDTO.class)
                .setParameter("fechaDefault", fechaDefault)
                .getResultList();
    }

}
