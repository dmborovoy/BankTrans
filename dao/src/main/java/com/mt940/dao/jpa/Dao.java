package com.mt940.dao.jpa;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.List;

public interface Dao<E, PK extends Serializable> {

    // Insert or update a contact
    public E save(E contact);

    /**
     * Make an instance managed and persistent.
     *
     * @param e entity instance
     */
    void persist(E e);

    /**
     * Merge the state of the given entity into the current persistence context.
     *
     * @param e entity instance
     * @return entity instance
     */
    E merge(E e);

    /**
     * Refresh the state of the instance from the database, overwriting changes made to the entity, if any.
     *
     * @param e entity instance
     */
    void refresh(E e);

    /**
     * Remove the entity instance.
     *
     * @param e entity instance
     */
    void delete(E e);

    /**
     * Find entity by primary key.
     *
     * @param id primary key
     * @return entity instance
     */
    E findById(PK id);

    /**
     * Lock an entity instance that is contained in the persistence context with the specified lock mode type.
     *
     * @param e            entity instance
     * @param lockModeType locking type
     */
    void lock(E e, LockModeType lockModeType);

    /**
     * Find all entities in current table.
     *
     * @return list of entity instances
     */
    List<E> findAll();

    /**
     * Synchronize the persistence context to the underlying database.
     */
    void flush();

    /**
     * Clear the persistence context, causing all managed entities to become detached.
     */
    void clear();

    /**
     * Create and execute query
     *
     * @param jpql string containing the query
     */
    void executeQuery(String jpql);

    /**
     * Total count of elements
     *
     * @return
     */
    long count();
}
