package org.example.service;

import org.example.entities.Carrera;
import org.example.entities.Estudiante;
import org.example.entities.Inscripcion;
import org.example.repository.MySqlDAOFactory;
import org.example.repository.mysql.BaseJPARepository;
import org.example.repository.mysql.MySqlEstudianteRepository;
import org.example.repository.mysql.MySqlInscripcionRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.Timestamp;

    public class InscripcionService {

        private final MySqlDAOFactory daoFactory;

        public InscripcionService(MySqlDAOFactory daoFactory) {
            this.daoFactory = daoFactory;
        }

        public void inscribirEstudianteEnCarrera(String documentoEstudiante, int idCarrera) {
            EntityManager em = daoFactory.getEm();
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                // 1. Obtener Repositorios
                MySqlEstudianteRepository estRepo = daoFactory.getEstudianteRepository();
                MySqlInscripcionRepository insRepo = daoFactory.getInscripcionRepository();

                // Usamos un Repositorio base genérico para Carrera (para evitar crear la clase MySqlCarreraRepository)
                BaseJPARepository<Carrera> carRepo = new BaseJPARepository<>(em, Carrera.class);

                // 2. Buscar Entidades
                int documento = Integer.parseInt(documentoEstudiante);
                Estudiante estudiante = estRepo.findById(documento);
                Carrera carrera = carRepo.findById(idCarrera);

                if (estudiante == null) {
                    throw new IllegalArgumentException("Estudiante no encontrado con DNI: " + documentoEstudiante);
                }
                if (carrera == null) {
                    throw new IllegalArgumentException("Carrera no encontrada con ID: " + idCarrera);
                }

                // 3. Crear Entidad Inscripcion
                Inscripcion nuevaInscripcion = new Inscripcion(
                        estudiante,
                        carrera,
                        new Timestamp(System.currentTimeMillis()), // fechaInscripcion (fecha y hora actual) ?
                        null, // fechaGraduacion (null al inicio) ?
                        0     // antiguedad (0 al inicio) ?
                );

                // 4. Mapear la relación en memoria (IMPORTANTE)
                estudiante.getInscripciones().add(nuevaInscripcion);
                carrera.getInscripciones().add(nuevaInscripcion);

                // 5. Persistir (el em.persist se encarga de guardar la nueva inscripción)
                insRepo.create(nuevaInscripcion);

                transaction.commit();
                System.out.println("Inscripción exitosa: " + estudiante.getNombre() + " en " + carrera.getNombre());
            } catch (Exception e) {
                if (transaction.isActive()) transaction.rollback();
                System.err.println("Error al inscribir al estudiante: " + e.getMessage());
                throw new RuntimeException("Fallo en la inscripción.", e);
            }
        }
    }

