package com.github.daniel.shuy.ws.rs.jpa.crud;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    public abstract EntityManager getEntityManager();

    public abstract Class<E> getEntityClass();

    public default void create(E e) {

        getEntityManager().persist(e);
    }

    public default List<E> findAll() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
        criteriaQuery.select(criteriaQuery.from(getEntityClass()));
        TypedQuery<E> query = getEntityManager().createQuery(criteriaQuery);

        return query.getResultList();
    }

    public default E find(Long id) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
        Root<E> root = criteriaQuery.from(getEntityClass());
        TypedQuery<E> query = getEntityManager().createQuery(criteriaQuery.where(criteriaBuilder.equal(root.get(EntityCRUD_.id), id)));

        E result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }
        return result;
    }

    public default void edit(E e) {

        getEntityManager().merge(e);
    }

    public default void remove(Long id) {
        final E e = find(id);

        if (e != null) {
            getEntityManager().remove(e);
        }
    }
}
