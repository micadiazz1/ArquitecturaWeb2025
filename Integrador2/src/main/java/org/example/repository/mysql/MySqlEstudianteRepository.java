package org.example.repository.mysql;


import org.example.DTO.EstudianteDTO;
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

    /*TODO:
    c) recuperar todos los estudiantes, y especificar algún criterio de ordenamiento simple.
     */
    public EstudianteDTO getEstudianteByLibreta(int numLibreta) {
        return getEntityManager()
                .createQuery("SELECT new EstudianteDTO(e.nombre, e.apellido, e.documento, e.numLibreta) FROM Estudiante e WHERE e.numLibreta = :numLibreta", EstudianteDTO.class)
                .setParameter("numLibreta", numLibreta)
                .getSingleResult();
    }


    public List<EstudianteDTO> getEstudiantesByGenero(Genero genero) {
        return getEntityManager().createQuery("SELECT new EstudianteDTO(e.nombre, e.apellido, e.documento, e.numLibreta) FROM Estudiante e WHERE e.genero = :genero", EstudianteDTO.class)
                .setParameter("genero", genero)
                .getResultList();
    }

}
