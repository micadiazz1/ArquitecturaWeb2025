package org.example;

import org.example.DTO.CarreraDTO;
import org.example.DTO.EstudianteDTO;
import org.example.DTO.ReporteCarreraDTO;
import org.example.entities.Carrera;
import org.example.repository.MySqlDAOFactory;
import org.example.repository.mysql.MySqlEstudianteRepository;
import org.example.repository.mysql.MySqlInscripcionRepository;
import org.example.service.EstudianteService;
import org.example.service.InscripcionService;
import org.example.utils.Genero;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    private static EntityManagerFactory emf;

    public static void main(String[] args) {

        emf = Persistence.createEntityManagerFactory("ArquiWebTp2");
        MySqlDAOFactory factory = MySqlDAOFactory.getInstance();

        try {
            DataLoader dataLoader = new DataLoader(emf);

            System.out.println("=========================================");
            System.out.println("        INICIO DE CARGA DE DATOS");
            System.out.println("=========================================");

            /*
            dataLoader.loadEstudiantes();
            dataLoader.loadCarreras();
            dataLoader.loadInscripciones();
            */

            System.out.println(">>> Datos cargados con éxito.\n");

            //---------------------------------------------------------
            // Alta estudiante
            //---------------------------------------------------------
            EstudianteService estudianteService = new EstudianteService(factory);
            EstudianteDTO nuevoEstudianteDTO = new EstudianteDTO(
                    "Juan", "Gomez", 34940294, 23124
            );

            System.out.println("=========================================");
            System.out.println(" CONSULTA A) Dar de Alta un Estudiante ");
            System.out.println("=========================================");
            //estudianteService.darDeAltaEstudiante(nuevoEstudianteDTO);
            System.out.println(">>> Estudiante dado de alta: " + nuevoEstudianteDTO + "\n");

            //---------------------------------------------------------
            // Alta inscripción
            //---------------------------------------------------------
            InscripcionService inscripcionService = new InscripcionService(factory);
            int dniEstudiante = 34940294;
            int idCarrera = 1;

            System.out.println("=========================================");
            System.out.println(" CONSULTA B) Dar de Alta una Inscripción ");
            System.out.println("=========================================");
            try {
                //inscripcionService.inscribirEstudianteEnCarrera(dniEstudiante, idCarrera);
                System.out.println(">>> Estudiante " + dniEstudiante + " inscripto en carrera ID: " + idCarrera + "\n");
            } catch (Exception e) {
                System.err.println("No se pudo inscribir al estudiante. Mensaje: " + e.getMessage());
            }

            //---------------------------------------------------------
            // Consultas de Repositorios
            //---------------------------------------------------------
            EntityManager em = emf.createEntityManager();
            MySqlEstudianteRepository estudianteRepo = MySqlEstudianteRepository.getInstance(em);
            MySqlInscripcionRepository inscripcionRepo = MySqlInscripcionRepository.getInstance(em);

            // C) Listado ordenado por apellido y nombre
            System.out.println("=========================================");
            System.out.println(" CONSULTA C) Listado de Estudiantes Ordenados ");
            System.out.println("=========================================");
            List<EstudianteDTO> estudiantesOrdenados = estudianteRepo.findAllOrderedByApellidoAndNombre();
            estudiantesOrdenados.forEach(e -> System.out.println(" - " + e));
            System.out.println();

            // D) Buscar estudiante por libreta
            System.out.println("=========================================");
            System.out.println(" CONSULTA D) Buscar Estudiante por Libreta ");
            System.out.println("=========================================");
            EstudianteDTO estudianteLibreta = estudianteRepo.getEstudianteByLibreta(12345);
            System.out.println(">>> Encontrado: " + estudianteLibreta + "\n");

            // E) Estudiantes por género
            System.out.println("=========================================");
            System.out.println(" CONSULTA E) Estudiantes por Género (MASCULINO) ");
            System.out.println("=========================================");
            List<EstudianteDTO> estudiantesGenero = estudianteRepo.getEstudiantesByGenero(Genero.MALE);
            estudiantesGenero.forEach(e -> System.out.println(" - " + e));
            System.out.println();

            // F) Carreras con inscriptos
            System.out.println("=========================================");
            System.out.println(" CONSULTA F) Carreras con cantidad de Inscriptos ");
            System.out.println("=========================================");
            List<CarreraDTO> carrerasConInscriptos = inscripcionRepo.getCarrerasConInscriptos();
            carrerasConInscriptos.forEach(c -> System.out.println(" - " + c));
            System.out.println();

            // G) Estudiantes por carrera y ciudad
            System.out.println("=========================================");
            System.out.println(" CONSULTA G) Estudiantes de una Carrera por Ciudad ");
            System.out.println("=========================================");
            Carrera carrera = em.find(Carrera.class, 1); // Ejemplo carrera con ID 1
            List<EstudianteDTO> estudiantesCarreraCiudad = inscripcionRepo.getEstudiantesByCarrera(carrera, "Rauch");
            estudiantesCarreraCiudad.forEach(e -> System.out.println(" - " + e));
            System.out.println();

            //2. C)
            // Ejecutar la función
            List<ReporteCarreraDTO> reporte = inscripcionRepo.generarReporteCarreras();

            // Mostrar resultados
            System.out.println("Reporte de Carreras:");
            System.out.println("-------------------------------------------------");
            System.out.printf("%-25s %-6s %-10s %-10s%n", "Carrera", "Año", "Inscriptos", "Egresados");
            System.out.println("-------------------------------------------------");

            for (ReporteCarreraDTO dto : reporte) {
                System.out.printf("%-25s %-6d %-10d %-10d%n",
                        dto.nombreCarrera(),
                        dto.anio(),
                        dto.inscriptos(),
                        dto.egresados());
            }

            em.close();

        } catch (Exception e) {
            System.err.println("Ocurrió un error en la carga: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }
}
