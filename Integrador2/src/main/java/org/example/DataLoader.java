package org.example;
import org.example.entities.Carrera;
import org.example.entities.Estudiante;
import org.example.entities.Inscripcion;
import org.example.utils.Genero;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;


public class DataLoader {

    private final EntityManagerFactory emf;

    public DataLoader(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private void executeLoad(Consumer<EntityManager> loaderLogic) {
        // Crea una nueva instancia de EntityManager.
        EntityManager em = emf.createEntityManager();
        // Obtiene el objeto de transacción asociado al EntityManager. Este objeto es el que te permite controlar el ciclo de vida de la transacción.
        EntityTransaction transaction = em.getTransaction();
        try {
            // Comienza una nueva transacción.
            transaction.begin();
            // Ejecuta la lógica de carga, que ya maneja las excepciones del CSV
            // Cede el control al CSVLoader para que inserte los datos.
            loaderLogic.accept(em);
            // Se confirman todos los cambios y se escriben permanentemente en la DB.
            transaction.commit();
        } catch (RuntimeException e) {
            // Captura RuntimeException (incluyendo las envueltas de IOException)
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error de carga de datos. Revise los archivos CSV.");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            // Manejo de otros errores de JPA/DB
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error en la transacción JPA: " + e.getMessage());
            throw new RuntimeException("Fallo en la transacción.", e);
        } finally {
            if (em != null) {
                em.close(); // Se cierra la conexión (EntityManager) para liberar recursos.
            }
        }
    }

    public void loadEstudiantes() {
        executeLoad(em -> {
            try { // TRY-CATCH para IOException
                CSVLoader<Estudiante> estudianteLoader = new CSVLoader<>(em);
                estudianteLoader.loadAndInsert("estudiantes.csv", record -> {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setDocumento(Integer.parseInt(record.get("DNI")));
                    estudiante.setNombre(record.get("nombre"));
                    estudiante.setApellido(record.get("apellido"));
                    estudiante.setEdad(Integer.parseInt(record.get("edad")));
                    estudiante.setGenero(Genero.valueOf(record.get("genero").toUpperCase().replace("-", "_")));
                    estudiante.setCiudad(record.get("ciudad"));
                    estudiante.setNumLibreta(Integer.parseInt(record.get("LU")));
                    return estudiante;
                });
            } catch (IOException e) {
                // Envuelve la excepción verificada en una no verificada (RuntimeException)
                throw new RuntimeException("Error de lectura en estudiantes.csv", e);
            }
        });
    }

    public void loadCarreras() {
        executeLoad(em -> {
            try { // TRY-CATCH para IOException
                CSVLoader<Carrera> carreraLoader = new CSVLoader<>(em);
                carreraLoader.loadAndInsert("carreras.csv", record -> {
                    Carrera carrera = new Carrera();
                    carrera.setNombre(record.get("carrera"));
                    carrera.setDuracion(Integer.parseInt(record.get("duracion")));
                    return carrera;
                });
            } catch (IOException e) {
                throw new RuntimeException("Error de lectura en carreras.csv", e);
            }
        });
    }

    public void loadInscripciones() {
        executeLoad(em -> {
            try { // TRY-CATCH para IOException
                CSVLoader<Inscripcion> inscripcionLoader = new CSVLoader<>(em);
                inscripcionLoader.loadAndInsert("estudianteCarrera.csv", record -> {
                    int documentoEstudiante = Integer.parseInt(record.get("id_estudiante"));
                    int idCarreraCsv = Integer.parseInt(record.get("id_carrera"));

                    Date inscripcionDate = null;
                    try {
                        inscripcionDate = new SimpleDateFormat("yyyy").parse(record.get("inscripcion"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Date graduacionDate = null;
                    String graduacionStr = record.get("graduacion");
                    if (graduacionStr != null && !graduacionStr.isEmpty()) {
                        try {
                            graduacionDate = new SimpleDateFormat("yyyy").parse(graduacionStr);
                        } catch (ParseException e) {
                            // Lógica de manejo de años fallidos
                            if (graduacionStr.length() == 4) {
                                try {
                                    graduacionDate = new SimpleDateFormat("yyyy").parse(graduacionStr);
                                } catch (ParseException e2) {
                                    // La fecha se deja como null
                                }
                            }
                        }
                    }

                    int antiguedad = Integer.parseInt(record.get("antiguedad"));

                    Estudiante estudiante = em.find(Estudiante.class, documentoEstudiante);
                    Carrera carrera = em.find(Carrera.class, idCarreraCsv); // Asume que idCarreraCsv es el ID de la DB

                    if (estudiante == null || carrera == null) {
                        System.err.println("ERROR: No se encontró estudiante con DNI " + documentoEstudiante +
                                " o carrera con ID " + idCarreraCsv + ". Saltando este registro.");
                        return null;
                    }

                    return new Inscripcion(
                            estudiante,
                            carrera,
                            new Timestamp(inscripcionDate.getTime()),
                            (graduacionDate != null) ? new Timestamp(graduacionDate.getTime()) : null,
                            antiguedad
                    );
                });
            } catch (IOException e) {
                throw new RuntimeException("Error de lectura en estudianteCarrera.csv", e);
            }
        });
    }
}
