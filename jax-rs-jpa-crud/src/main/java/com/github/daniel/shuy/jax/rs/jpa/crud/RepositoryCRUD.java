package com.github.daniel.shuy.jax.rs.jpa.crud;

import com.github.daniel.shuy.JPAUtils;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Extend this class to create a CRUD RepositoryCRUD Class.
 * @param <E> The Entity Class type
 */
public abstract class RepositoryCRUD<E extends EntityCRUD> {
    private final JPAUtils jpaUtils;
    private final Class<E> clazz;
    
    public RepositoryCRUD(EntityManagerFactory entityManagerFactory, Class<E> clazz) {
        this.jpaUtils = new JPAUtils(entityManagerFactory);
        this.clazz = clazz;
    }
    
    public void add(E e) {
        jpaUtils.executeQuery((entityManager) -> {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.persist(e);
            transaction.commit();
        });
    }
    
    public List<E> getAll() {
        return jpaUtils.executeQuery((entityManager) -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(clazz);
            criteriaQuery.select(criteriaQuery.from(clazz));
            TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
            
            return query.getResultList();
        });
    }
    
    public E get(Long id) {
        return jpaUtils.executeQuery((entityManager) -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(clazz);
            Root<E> root = criteriaQuery.from(clazz);
            TypedQuery<E> query = entityManager.createQuery(criteriaQuery.where(criteriaBuilder.equal(root.get(EntityCRUD_.id), id)));
            
            E result;
            try {
                result = query.getSingleResult();
            }
            catch (NoResultException e) {
                result = null;
            }
            return result;
        });
    }
    
    public void update(E e) {
        jpaUtils.executeQuery((entityManager) -> {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.merge(e);
            transaction.commit();
        });
    }
    
    public void remove(E e) {
        jpaUtils.executeQuery((entityManager) -> {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.remove(e);
            transaction.commit();
        });
    }
}
