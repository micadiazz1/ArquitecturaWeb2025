package org.example.repository.mysql;


import org.example.entities.Estudiante;
import org.example.utils.Genero;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import org.example.entities.Estudiante;
public class MySqlEstudianteRepository extends BaseJPARepository<Estudiante> {
    private static volatile MySqlEstudianteRepository instance;


    public MySqlEstudianteRepository(EntityManager em) {
        super(em, Estudiante.class );
    }


    public static MySqlEstudianteRepository getInstance(EntityManager em){
        if (instance == null){
            instance = new MySqlEstudianteRepository(em);
        }
        return instance;
    }
    public Estudiante getEstudianteByLibreta(int numLibreta) {
        return getEntityManager()
                .createQuery("SELECT e FROM Estudiante e WHERE e.numLibreta = :numLibreta", Estudiante.class)
                .setParameter("numLibreta", numLibreta)
                .getSingleResult();
    }


    public List<Estudiante> getEstudiantesByGenero(Genero genero) {
        return getEntityManager().createQuery("SELECT e FROM Estudiante e WHERE e.genero = :genero", Estudiante.class)
                .setParameter("genero", genero)
                .getResultList();
    }

}
