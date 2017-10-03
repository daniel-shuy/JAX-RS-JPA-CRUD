package com.github.daniel.shuy.ws.rs.jpa.crud;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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

    public default void add(E e) {
        EntityTransaction transaction = getEntityManager().getTransaction();

        transaction.begin();
        getEntityManager().persist(e);
        transaction.commit();
    }

    public default List<E> getAll() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
        criteriaQuery.select(criteriaQuery.from(getEntityClass()));
        TypedQuery<E> query = getEntityManager().createQuery(criteriaQuery);

        return query.getResultList();
    }

    public default E get(Long id) {
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

    public default void update(E e) {
        EntityTransaction transaction = getEntityManager().getTransaction();

        transaction.begin();
        getEntityManager().merge(e);
        transaction.commit();
    }

    public default void remove(Long id) {
        final E e = get(id);

        if (e != null) {
            EntityTransaction transaction = getEntityManager().getTransaction();

            transaction.begin();
            getEntityManager().remove(e);
            transaction.commit();
        }
    }
}
