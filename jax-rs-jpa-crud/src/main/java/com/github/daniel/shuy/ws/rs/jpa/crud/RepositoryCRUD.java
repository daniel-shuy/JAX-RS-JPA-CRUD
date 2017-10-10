package com.github.daniel.shuy.ws.rs.jpa.crud;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Extend this class to create a CRUD Repository Class.
 *
 * @param <E> The Entity Class type
 */
public interface RepositoryCRUD<E extends EntityCRUD> {
    public abstract EntityManager getEntityManager();

    public abstract Class<E> getEntityClass();

    public default void create(E e) {
        EntityManager entityManager = getEntityManager();

        entityManager.persist(e);
    }

    public default List<E> findAll() {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        criteriaQuery.select(criteriaQuery.from(getEntityClass()));
        TypedQuery<E> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }

    public default E find(Long id) {
        EntityManager entityManager = getEntityManager();

        return entityManager.find(getEntityClass(), id);
    }

    public default void edit(E e) {
        EntityManager entityManager = getEntityManager();

        entityManager.merge(e);
    }

    public default void remove(Long id) {
        EntityManager entityManager = getEntityManager();

        final E e = find(id);

        if (e != null) {
            entityManager.remove(e);
        }
    }
}
