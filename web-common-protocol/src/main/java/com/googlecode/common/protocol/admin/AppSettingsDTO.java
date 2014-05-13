
package com.googlecode.common.protocol.admin;

import com.googlecode.common.protocol.perm.PermissionNodeDTO;


/**
 * Part of server requests/responses, containing application settings info.
 */
public final class AppSettingsDTO {

    private PermissionNodeDTO   permissions;

    
    public AppSettingsDTO() {
    }

    public AppSettingsDTO(PermissionNodeDTO permissions) {
        this.permissions = permissions;
    }
    
    public PermissionNodeDTO getPermissions() {
        return permissions;
    }
    
    public void setPermissions(PermissionNodeDTO permissions) {
        this.permissions = permissions;
    }

}
