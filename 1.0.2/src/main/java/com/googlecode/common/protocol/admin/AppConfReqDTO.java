
package com.googlecode.common.protocol.admin;

import com.googlecode.common.protocol.perm.PermissionNodeDTO;


/**
 * Contains configuration request info.
 */
public final class AppConfReqDTO {

    private PermissionNodeDTO   permissions;
    private Boolean             loadSystemInfo;

    
    public AppConfReqDTO() {
    }
    
    public AppConfReqDTO(PermissionNodeDTO permissions) {
        this.permissions = permissions;
    }
    
    public PermissionNodeDTO getPermissions() {
        return permissions;
    }
    
    public void setPermissions(PermissionNodeDTO permissions) {
        this.permissions = permissions;
    }
    
    public boolean safeIsLoadSystemInfo() {
        return (loadSystemInfo != null ? 
                loadSystemInfo.booleanValue() : false);
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeIsLoadSystemInfo()
     */
    @Deprecated
    public Boolean getLoadSystemInfo() {
        return loadSystemInfo;
    }
    
    public void setLoadSystemInfo(Boolean loadSystemInfo) {
        this.loadSystemInfo = loadSystemInfo;
    }

}
