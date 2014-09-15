
package com.googlecode.common.dao;

import java.io.Serializable;
import java.util.List;


/**
 * Generic dao interface.
 *
 * @param <T> entity type
 * @param <K> key type
 */
public interface GenericDao<T, K extends Serializable> {

    /**
     * Returns entity by it's identifier.
     * 
     * @param key   entity identifier in the database
     * @return      entity stored under specified key or <tt>null</tt> if none
     */
    public T get(K key);

    /**
     * Returns paged entity list. Objects are retrieved from
     * firstResult index and to firstResult + maxResults index.
     * 
     * @param firstResult   first entity index to retrieve
     * @param maxResults    maximum number of entities that has to be retrieved
     * @return              list of entities in the range
     */
    public List<T> get(int firstResult, int maxResults);
    
    /**
     * Returns entities total count.
     * 
     * @return entities count
     */
    public long getCount();

    /**
     * Returns all entities
     * @return all entities list
     */
    public List<T> getAll();
    
    /**
     * Insert the record into the database.
     * 
     * @param entity    record to save
     */
    public void save(T entity);

    /**
     * Update the record in DB.
     * 
     * @param entity    entity to update
     * @return          updated record
     */
    public T merge(T entity);
    
    /**
     * Deletes specifier entry from the database.
     * 
     * @param entity record to delete
     */
    public void delete(T entity);

}
