
package com.googlecode.common.dao.domain;


/**
 * Generic interface for dictionary entities.
 */
public interface DictEntity {
    
    /**
     * Returns entity id.
     * @return entity id
     */
    public int getId();
    
    /**
     * Sets entity id.
     * 
     * @param id    entity id
     */
    public void setId(int id);

    /**
     * Returns entity name.
     * @return entity name
     */
    public String getName();

    /**
     * Sets entity name.
     * 
     * @param name  entity name
     */
    public void setName(String name);

    /**
     * Returns sequence name for this dictionary entity.
     * @return sequence name for this dictionary entity
     */
    public String getSequenceName();
    
}
