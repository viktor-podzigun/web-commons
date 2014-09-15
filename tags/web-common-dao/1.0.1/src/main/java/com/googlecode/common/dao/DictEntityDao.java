
package com.googlecode.common.dao;

import java.io.Serializable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import com.googlecode.common.dao.domain.DictEntity;


/**
 * Generic DAO interface for dictionary entities.
 */
@NoRepositoryBean
public interface DictEntityDao<T extends DictEntity, K extends Serializable> 
        extends Repository<T, K> {

    /**
     * Retrieves an entity by its id.
     * 
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    public T findOne(K id);

    /**
     * Returns entity by it's name.
     *
     * @param name  entity's name
     * @return      entity or <tt>null</tt> if no such found
     */
    public T findByName(String name);

    /**
     * Returns all instances of the type, sorted.
     * 
     * @param sort  sorting parameters
     * @return      all entities
     */
    public Iterable<T> findAll(Sort sort);

    /**
     * Saves a given entity. Use the returned instance for further operations 
     * as the save operation might have changed the entity instance completely.
     * 
     * @param entity
     * @return the saved entity
     */
    public <S extends T> S save(S entity);

}
