package org.example.service;

import org.example.DTO.EstudianteDTO;
import org.example.entities.Estudiante;
import org.example.repository.MySqlDAOFactory;
import org.example.repository.mysql.MySqlEstudianteRepository;
import org.example.utils.Genero;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.HashSet;

public class EstudianteService {

        private final MySqlDAOFactory daoFactory;

        public EstudianteService(MySqlDAOFactory daoFactory) {
            this.daoFactory = daoFactory;
        }

        public void darDeAltaEstudiante(EstudianteDTO dto) {
            // Obtenemos el EntityManager de la fábrica para esta operación (simplificación)
            EntityManager em = daoFactory.getEm();
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                MySqlEstudianteRepository repo = daoFactory.getEstudianteRepository();

                // 1. Conversión DTO -> Entidad
                Estudiante nuevoEstudiante = convertirAEntidad(dto);

                // 2. Persistencia. El repo.create() llama a em.persist()
                repo.create(nuevoEstudiante);

                transaction.commit();
                System.out.println("Estudiante dado de alta con DNI: " + dto.dni());
            } catch (Exception e) {
                if (transaction.isActive()) transaction.rollback();
                System.err.println("Error al dar de alta al estudiante: " + e.getMessage());
                throw new RuntimeException("Fallo en el alta de Estudiante.", e);
            }
        }

        private Estudiante convertirAEntidad(EstudianteDTO dto) {
            Estudiante e = new Estudiante();

            // 1. Mapeo de los 4 campos obligatorios de entrada (vienen del DTO) criterio?
            e.setDocumento(dto.dni());
            e.setNumLibreta(dto.numLibreta());
            e.setNombre(dto.nombre());
            e.setApellido(dto.apellido());

            // 2. Asignación de Valores por Defecto (Para campos que no están en el DTO pero son necesarios para la Entidad/DB)
            e.setEdad(18); // Valor por defecto ?
            e.setCiudad("No Registrada");
            e.setGenero(Genero.DESCONOCIDO); // Usamos un valor del Enum  DESCONOCIDO como defecto
            e.setInscripciones(new HashSet<>()); // Inicializa la colección de inscripciones como vacía

            // 3. Devuelve la entidad Estudiante
            return e;
        }
    }

