package com.mt940.dao.jpa;


import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractDao<E, PK extends Serializable> implements Dao<E, PK> {

    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public abstract Class<E> getEntityClass();

    @Override
    public void persist(E e) {
        getEntityManager().persist(e);
    }

    @Override
    public E merge(E e) {
        return getEntityManager().merge(e);
    }

    @Override
    public void refresh(E e) {
        getEntityManager().refresh(e);
    }

    @Override
    public void delete(E e) {
        getEntityManager().remove(e);
    }

    @Override
    public E findById(PK id) {
        return getEntityManager().find(getEntityClass(), id);
    }

    @Override
    public void lock(E e, LockModeType lockModeType) {
        getEntityManager().lock(e, lockModeType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        return getEntityManager().createQuery("from " + getEntityClass().getSimpleName()).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public long count() {
        return getEntityManager().createQuery("from " + getEntityClass().getSimpleName()).getMaxResults();
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }

    public void executeQuery(String jpql) {
        getEntityManager().createQuery(jpql).executeUpdate();
    }

    @Override
    public void clear() {
        getEntityManager().clear();
    }
}
