
package com.googlecode.common.protocol.perm;

import java.util.Collections;
import java.util.List;


/**
 * Part of server requests and responses, containing permission node info.
 */
public final class PermissionNodeDTO {

    private String                  name;
    private String                  title;
    
    private List<PermissionDTO>     permissions;
    private List<PermissionNodeDTO> nodes;
    
    
    public PermissionNodeDTO() {
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
    
    public List<PermissionDTO> safeGetPermissions() {
        if (permissions == null) {
            return Collections.emptyList();
        }
        
        return permissions;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeGetPermissions()
     */
    @Deprecated
    public List<PermissionDTO> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
    
    public List<PermissionNodeDTO> safeGetNodes() {
        if (nodes == null) {
            return Collections.emptyList();
        }
        
        return nodes;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeGetNodes()
     */
    @Deprecated
    public List<PermissionNodeDTO> getNodes() {
        return nodes;
    }
    
    public void setNodes(List<PermissionNodeDTO> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{name: " + name
                + (title != null ? ", title: " + title : "") 
                + (permissions != null ? ", permissionsCount: " + permissions.size() : "")
                + (nodes != null ? ", nodesCount: " + nodes.size() : "") 
                + "}";
    }

}
