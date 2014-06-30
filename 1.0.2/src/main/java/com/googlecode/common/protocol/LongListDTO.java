
package com.googlecode.common.protocol;

import java.util.Collections;
import java.util.List;


/**
 * Common DTO for requests that contains long id list.
 */
public final class LongListDTO {

    private List<Long>  list;
    
    
    public LongListDTO() {
    }
    
    public LongListDTO(List<Long> list) {
        this.list = list;
    }
    
    public List<Long> safeGetList() {
        if (list == null) {
            return Collections.emptyList();
        }
        
        return list;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetList()
     */
    @Deprecated
    public List<Long> getList() {
        return list;
    }
    
    public void setList(List<Long> list) {
        this.list = list;
    }
    
    protected String paramString() {
        return (list != null ? "list: " + list : "");
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "{" + paramString() + "}";
    }

}
