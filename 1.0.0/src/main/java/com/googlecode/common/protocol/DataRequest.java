
package com.googlecode.common.protocol;

import java.util.Collections;
import java.util.List;


/**
 * Generic request for sending data of any type.
 */
public class DataRequest<F> {
    
    public static final int DEFAULT_LIMIT = 25;

    private List<SortInfo>  sortInfo;
    
    private F               filterInfo;
    
    /** Index of requested row, started from 0 */
    private Integer         startIndex;
    
    /** Max number of rows on one page */
    private Integer         limit;
    
    
    public DataRequest() {
    }
    
    public List<SortInfo> safeGetSortInfo() {
        if (sortInfo == null) {
            return Collections.emptyList();
        }
        
        return sortInfo;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetSortInfo()
     */
    @Deprecated
    public List<SortInfo> getSortInfo() {
        return sortInfo;
    }
    
    public void setSortInfo(List<SortInfo> sortInfo) {
        this.sortInfo = sortInfo;
    }

    public F getFilterInfo() {
        return filterInfo;
    }
    
    public void setFilterInfo(F filterInfo) {
        this.filterInfo = filterInfo;
    }
    
    public int safeGetStartIndex() {
        return (startIndex != null ? startIndex.intValue() : 0);
    }
    
    public Integer getStartIndex() {
        return startIndex;
    }
    
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }
    
    public int safeGetLimit() {
        return (limit != null ? limit.intValue() : DEFAULT_LIMIT);
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetLimit()
     */
    @Deprecated
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

}
