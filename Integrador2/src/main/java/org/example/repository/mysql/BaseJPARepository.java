package org.example.repository.mysql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import org.example.entities.Inscripcion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.util.List;


public class BaseJPARepository<T> implements GenericRepository<T> {
    private EntityManager em;
    private Class<T> entityClass;

    public BaseJPARepository(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    @Override
    public void delete(T entity) {
        T managedEntity = getEntityManager().merge(entity);
        getEntityManager().remove(managedEntity);
    }

    @Override
    public void update(T entity) {
        getEntityManager().merge(entity);
    }

    @Override
    public T findById(Integer id) {
        return getEntityManager().find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        return getEntityManager().createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }
}