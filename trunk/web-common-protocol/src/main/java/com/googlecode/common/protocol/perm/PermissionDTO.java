
package com.googlecode.common.protocol.perm;

import java.util.Collections;
import java.util.List;


/**
 * Part of server requests and responses, containing permission info.
 */
public final class PermissionDTO {

    private Long            id;
    
    private String          name;
    private String          title;
    
    private Boolean         allowed;
    private List<Integer>   roles;

    
    public PermissionDTO() {
    }
    
    public long safeGetId() {
        return (id != null ? id.longValue() : 0L);
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeGetId()
     */
    @Deprecated
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public boolean safeIsAllowed() {
        return (allowed != null ? allowed.booleanValue() : false);
    }

    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeIsAllowed()
     */
    @Deprecated
    public Boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(Boolean isAllowed) {
        this.allowed = isAllowed;
    }

    public List<Integer> safeGetRoles() {
        if (roles == null) {
            return Collections.emptyList();
        }
        
        return roles;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetRoles()
     */
    @Deprecated
    public List<Integer> getRoles() {
        return roles;
    }
    
    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "{name: " + name
                + (title != null ? ", title: " + title : "")
                + (allowed != null ? ", allowed: " + allowed : "")
                + (id != null ? ", id: " + id : "")
                + "}";
    }

}
