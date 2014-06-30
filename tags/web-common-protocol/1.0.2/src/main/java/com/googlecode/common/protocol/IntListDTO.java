
package com.googlecode.common.protocol;

import java.util.Collections;
import java.util.List;


/**
 * Common DTO for requests that contains integer id list.
 */
public final class IntListDTO {

    private List<Integer>   list;
    
    
    public IntListDTO() {
    }
    
    public IntListDTO(List<Integer> list) {
        this.list = list;
    }
    
    public List<Integer> safeGetList() {
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
    public List<Integer> getList() {
        return list;
    }
    
    public void setList(List<Integer> list) {
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
