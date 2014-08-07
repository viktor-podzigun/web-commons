
package com.googlecode.common.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.googlecode.common.dao.DictEntityDao;
import com.googlecode.common.dao.SequenceDao;
import com.googlecode.common.dao.domain.DictEntity;
import com.googlecode.common.protocol.dict.DictDTO;
import com.googlecode.common.service.DictEntityService;


/**
 * Default implementation for {@link DictEntityService} interface.
 */
@Service
@Lazy
public class DictEntityServiceImpl implements DictEntityService {

    private static final Sort   DICT_SORT_ASC = new Sort(new Sort.Order(
            Sort.Direction.ASC, "name"));
    
    private final Logger        log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private SequenceDao         sequenceDao;
    
    
    @Override
    public <T extends DictEntity> void loadDictionary(String dictName, 
            ConcurrentMap<String, T> map, DictEntityDao<T, Integer> dao) {
        
        for (T entity : dao.findAll(DICT_SORT_ASC)) {
            map.put(entity.getName(), entity);
        }
        
        log.info("Loaded " + map.size() + " " + dictName);
    }
    
    @Override
    public <T extends DictEntity> List<DictDTO> getAll(
            DictEntityDao<T, Integer> dao) {
        
        List<DictDTO> list = new ArrayList<DictDTO>();
        for (DictEntity d : dao.findAll(DICT_SORT_ASC)) {
            list.add(new DictDTO(d.getId(), d.getName()));
        }
        
        return list;
    }
    
    @Override
    public <T extends DictEntity> T getMapEntity(T newEntity, 
            ConcurrentMap<String, T> map, DictEntityDao<T, Integer> dao) {
        
        String name = newEntity.getName();
        if (name == null) {
            throw new IllegalArgumentException("Entity name is not specified");
        }
        
        T entity = map.get(name);
        if (entity != null) {
            return entity;
        }
        
        int retryCount = 5;
        while ((entity = dao.findByName(name)) == null && retryCount-- > 0) {
            try {
                if (newEntity.getId() == 0) {
                    newEntity.setId(sequenceDao.nextInt(
                            newEntity.getSequenceName()));
                }
                
                dao.save(newEntity);
                
            } catch (DataIntegrityViolationException x) {
                // entity already created in another parallel transaction
                log.warn("Dictionary entity already created"
                        + ", dict: " + newEntity.getSequenceName()
                        + ", name: " + name);
            }
        }
        
        if (entity == null) {
            throw new IllegalStateException("Cannot save dictionary entity: " 
                    + newEntity.getSequenceName());
        }
        
        map.put(name, entity);
        return entity;
    }
    
    @Override
    public <T extends DictEntity> T getById(int id, 
            ConcurrentMap<String, T> map, DictEntityDao<T, Integer> dao) {
        
        if (id == 0) {
            return null;
        }
        
        for (Map.Entry<String, T> entry : map.entrySet()) {
            T entity = entry.getValue();
            if (id == (int)entity.getId()) {
                return entity;
            }
        }
        
        T entity = dao.findOne(id);
        if (entity != null) {
            // cache dictionary entity
            map.put(entity.getName(), entity);
            return entity;
        }
        
        return null;
    }
    
    @Override
    public <T extends DictEntity> T updateDict(T entity,
            ConcurrentMap<String, T> map, DictEntityDao<T, Integer> dao) {
        
        int id = entity.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Entity id is not specified");
        }
        
        String name = entity.getName();
        if (name == null) {
            throw new IllegalArgumentException("Entity name is not specified");
        }
        
        entity = dao.save(entity);
        map.put(name, entity);
        return entity;
    }

}
