package org.example;

import org.example.DTO.EstudianteDTO;
import org.example.repository.MySqlDAOFactory;
import org.example.service.EstudianteService;
import org.example.service.InscripcionService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    private static EntityManagerFactory emf;

    public static void main(String[] args) {

        // 1. Crea el EntityManagerFactory (emf).
        emf = Persistence.createEntityManagerFactory("ArquiWebTp2");

        // 2. Inicializa el DAOFactory
        MySqlDAOFactory factory = MySqlDAOFactory.getInstance();
        try {
            // 3. Crea una instancia del Gestor de Carga (DataLoader) y se le da la fábrica para que pueda crear conexiones.
            DataLoader dataLoader = new DataLoader(emf);

            System.out.println("Iniciando carga de datos...");

            // 4. Llamado al metodo de carga (se inicia el proceso de carga de estudiantes, carreras e inscripciones)
            dataLoader.loadEstudiantes();
            dataLoader.loadCarreras();
            dataLoader.loadInscripciones();

            System.out.println("Datos cargados con éxito.");

            //---------------------------------------------------------
            // INICIO: IMPLEMENTACIÓN CONSULTA (DAR DE ALTA ESTUDIANTE)
            //---------------------------------------------------------

            EstudianteService estudianteService = new EstudianteService(factory);

            // Datos del nuevo estudiante
            EstudianteDTO nuevoEstudianteDTO = new EstudianteDTO(
                    "Carlos",
                    "Gomez",
                    "40111222",
                    "12345"
            );

            System.out.println("\n--- Consulta: Dar de Alta un Estudiante ---");
            estudianteService.darDeAltaEstudiante(nuevoEstudianteDTO);

            //---------------------------------------------------------
            // INICIO: IMPLEMENTACIÓN CONSULTA (DAR DE ALTA INSCRIPCION)
            //---------------------------------------------------------

            InscripcionService inscripcionService = new InscripcionService(factory);

            // Prueba con la Carrera con ID 1 y el Estudiante con DNI 40111222 que existen.
            String dniEstudiante = "40111222";
            int idCarrera = 1;

            System.out.println("\n--- Consulta: Dar de Alta una Inscripción ---");

            try {
                inscripcionService.inscribirEstudianteEnCarrera(dniEstudiante, idCarrera);
            } catch (Exception e) {
                System.err.println("No se pudo inscribir al estudiante. Mensaje: " + e.getMessage());
            }


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


