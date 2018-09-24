package com.github.daniel.shuy.ws.rs.jpa.crud;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Extend this class to create a CRUD Repository Class.
 *
 * @param <E> The Entity Class type
 */
public interface RepositoryCRUD<E extends EntityCRUD> {
    /**
     * Override this method to provide a {@link EntityManager} instance.
     * 
     * @return A {@link EntityManager} instance.
     */
    public abstract EntityManager getEntityManager();

    /**
     * Override this method to provide the {@link Class} of the JPA Entity.
     * 
     * @return The {@link Class} of the JPA Entity.
     */
    public abstract Class<E> getEntityClass();

    public default E create(E e) {
        EntityManager entityManager = getEntityManager();

        // use EntityManager#merge(T) instead of EntityManager#persist(T)
        // so that entity will be refreshed
        return entityManager.merge(e);
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

    public default List<E> findRange(Long from, Long to) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        criteriaQuery.select(criteriaQuery.from(getEntityClass()));
        TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(Math.toIntExact(to - from + 1));
        query.setFirstResult(Math.toIntExact(from));

        return query.getResultList();
    }

    public default long count() {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<E> root = criteriaQuery.from(getEntityClass());
        criteriaQuery.select(criteriaBuilder.count(root));
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);

        return query.getSingleResult();
    }

    public default E edit(E e) {
        EntityManager entityManager = getEntityManager();

        return entityManager.merge(e);
    }

    public default void remove(Long id) {
        EntityManager entityManager = getEntityManager();

        final E e = find(id);

        if (e != null) {
            entityManager.remove(e);
        }
    }
}
