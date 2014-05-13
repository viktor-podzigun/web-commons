
package com.googlecode.common.dao;

import java.util.List;


/**
 * Generic page entities list.
 * 
 * <p>Contains optional total rows count field for paging.
 */
public final class PageData<T> {
    
    private final List<T>   entities;
    private final Long      totalCount;

    
    public PageData(List<T> entities) {
        this(entities, null);
    }

    public PageData(List<T> entities, Long totalCount) {
        this.entities   = entities;
        this.totalCount = totalCount;
    }
    
    public List<T> getEntities() {
        return entities;
    }
    
    public Integer getTotalCount() {
        return (totalCount != null ? totalCount.intValue() : null);
    }

    public int safeGetTotalCount() {
        return (totalCount != null ? totalCount.intValue() : 0);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{entitiesCount: " + entities.size()
                + (totalCount != null ? ", totalCount: " + totalCount : "") 
                + "}";
    }

}
