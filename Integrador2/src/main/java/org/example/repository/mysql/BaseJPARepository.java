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
            EntityManager em = getEntityManager();
            em.getTransaction().begin();
            if (!(entity instanceof Inscripcion)) {
                em.persist(entity);
            } else {
                em.merge(entity);
            }
            em.getTransaction().commit();
        }

        @Override
        public void delete(T entity) {
            EntityManager em = getEntityManager();
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        }

        @Override
        public void update(T entity) {
            EntityManager em = getEntityManager();
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
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