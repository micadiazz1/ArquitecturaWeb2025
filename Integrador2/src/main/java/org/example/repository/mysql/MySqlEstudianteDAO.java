package org.example.repository.mysql;

import org.example.DAO.EstudianteDAO;
import org.example.entities.Carrera;
import org.example.entities.Ciudad;
import org.example.entities.Estudiante;
import org.example.utils.Genero;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class MySqlEstudianteDAO implements EstudianteDAO {

    private static volatile MySqlEstudianteDAO instance;
    private EntityManager em;

    private MySqlEstudianteDAO(EntityManager em) {
        this.em = em;
    }

    public static MySqlEstudianteDAO getInstance(EntityManager em) {
        return instance == null ? instance = new MySqlEstudianteDAO(em) : instance;
    }

    /**
     * a) dar de alta un estudiante
     * */
    @Override
    public void addEstudiante(Estudiante estudiante) {
        em.persist(estudiante);
    }

    /**
     * c) recuperar todos los estudiantes, y especificar algún criterio de ordenamiento simple.
     * */
    @Override
    public List<Estudiante> getEstudiantes() {
        return em.createNamedQuery("Estudiante.findAllOrderByNombreApellido", Estudiante.class)
                .getResultList();
    }

    @Override
    public Estudiante getEstudianteByLibreta(int numLibreta) {
        return em.createQuery("SELECT e FROM Estudiante e WHERE e.numLibreta = :numLibreta", Estudiante.class)
                .setParameter("numLibreta", numLibreta)
                .getSingleResult();
    }

    @Override
    public List<Estudiante> getEstudiantesByGenero(Genero genero) {
        return em.createQuery("SELECT e FROM Estudiante e WHERE e.genero = :genero", Estudiante.class)
                .setParameter("genero", genero)
                .getResultList();
    }
}
