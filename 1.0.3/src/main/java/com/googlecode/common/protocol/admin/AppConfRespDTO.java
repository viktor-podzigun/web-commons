
package com.googlecode.common.protocol.admin;

import java.util.Collections;
import java.util.List;
import com.googlecode.common.protocol.perm.PermissionNodeDTO;


/**
 * Contains configuration response info.
 */
public final class AppConfRespDTO {

    private PermissionNodeDTO   permissions;
    
    private List<AppSystemDTO>  systems;

    
    public AppConfRespDTO() {
    }

    public PermissionNodeDTO getPermissions() {
        return permissions;
    }
    
    public void setPermissions(PermissionNodeDTO permissions) {
        this.permissions = permissions;
    }
    
    public List<AppSystemDTO> safeGetSystems() {
        if (systems == null) {
            return Collections.emptyList();
        }
        
        return systems;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetSystems()
     */
    @Deprecated
    public List<AppSystemDTO> getSystems() {
        return systems;
    }
    
    public void setSystems(List<AppSystemDTO> systems) {
        this.systems = systems;
    }

}
