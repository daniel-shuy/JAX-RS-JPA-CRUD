package com.github.daniel.shuy.ws.rs.jpa.crud;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.New;
import javax.inject.Inject;
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
public abstract class RepositoryCRUD<E extends EntityCRUD> {
    @Inject
    private EntityManager entityManager;

    @Inject
    @New
    private Instance<E> entity;

    private Class<? extends EntityCRUD> clazz;

    @PostConstruct
    void postConstruct() {
        clazz = entity.get().getClass();
    }

    public void add(E e) {
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        entityManager.persist(e);
        transaction.commit();
    }

    public List<E> getAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(clazz);
        criteriaQuery.select(criteriaQuery.from(clazz));
        TypedQuery<E> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }

    public E get(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<E> root = criteriaQuery.from(clazz);
        TypedQuery<E> query = entityManager.createQuery(criteriaQuery.where(criteriaBuilder.equal(root.get(EntityCRUD_.id), id)));

        E result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }
        return result;
    }

    public void update(E e) {
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        entityManager.merge(e);
        transaction.commit();
    }

    public void remove(Long id) {
        final E e = get(id);

        if (e != null) {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.remove(e);
            transaction.commit();
        }
    }
}
