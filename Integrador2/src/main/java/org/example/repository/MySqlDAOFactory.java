package org.example.repository;

import org.example.repository.mysql.MySqlEstudianteDAO;
import org.example.repository.mysql.MySqlInscripcionDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MySqlDAOFactory {

    private EntityManagerFactory emf;
    private EntityManager em;
    private static volatile MySqlDAOFactory instance;

    private MySqlDAOFactory() {
        this.emf = Persistence.createEntityManagerFactory("ArquiWebTp2");
        this.em = emf.createEntityManager();
    }

    public static MySqlDAOFactory getInstance() {
        return instance == null ? instance = new MySqlDAOFactory() : instance;
    }

    public MySqlEstudianteDAO getEstudianteDAO() {
        return MySqlEstudianteDAO.getInstance(this.em);
    }

    public MySqlInscripcionDAO getInscripcionDAO() {
        return MySqlInscripcionDAO.getInstance(this.em);
    }
}
