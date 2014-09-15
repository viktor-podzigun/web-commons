
package com.googlecode.common.service;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import com.googlecode.common.dao.DictEntityDao;
import com.googlecode.common.dao.domain.DictEntity;
import com.googlecode.common.protocol.dict.DictDTO;


/**
 * Service for working with {@link DictEntity} entities.
 */
public interface DictEntityService {

    /**
     * Loads all dictionary data to the specified map from the specified dao.
     * 
     * @param dictName  dictionary name
     * @param map       dictionary cache map
     * @param dao       dictionary dao
     */
    public <T extends DictEntity> void loadDictionary(String dictName, 
            ConcurrentMap<String, T> map, DictEntityDao<T, Integer> dao);
    
    /**
     * Converts all dictionary data and returns dto list.
     * 
     * @param dao       dictionary dao
     * @return          dictionary dto list
     */
    public <T extends DictEntity> List<DictDTO> getAll(
            DictEntityDao<T, Integer> dao);
    
    /**
     * Maps the given dictionary entity by name.
     * 
     * @param newEntity dictionary entity with name
     * @param map       dictionary cache map
     * @param dao       dictionary dao
     * @return          mapped entity with id
     */
    public <T extends DictEntity> T getMapEntity(T newEntity, 
            ConcurrentMap<String, T> map, DictEntityDao<T, Integer> dao);
    
    /**
     * Returns dictionary entity by the given id.
     * 
     * @param id        dictionary entity's id
     * @param map       dictionary cache map
     * @param dao       dictionary dao
     * @return          dictionary entity
     */
    public <T extends DictEntity> T getById(int id, 
            ConcurrentMap<String, T> map, DictEntityDao<T, Integer> dao);

    /**
     * Updates the given entity in the given cache and db.
     * 
     * @param entity    dictionary entity
     * @param map       dictionary cache map
     * @param dao       dictionary dao
     * @return          dictionary entity
     */
    public <T extends DictEntity> T updateDict(T entity, 
            ConcurrentMap<String, T> map, DictEntityDao<T, Integer> dao);

}
