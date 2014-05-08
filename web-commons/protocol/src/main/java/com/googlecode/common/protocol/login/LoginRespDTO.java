
package com.googlecode.common.protocol.login;

import java.util.Collections;
import java.util.List;
import com.googlecode.common.protocol.perm.PermissionNodeDTO;


/**
 * Part of server responses, containing login user data.
 */
public final class LoginRespDTO {

    private Boolean         superUser;
    private List<Integer>   roles;
    private List<PermissionNodeDTO> permissions;
    
    private String          login;
    private String          token;
    
    private AppMenuDTO      appMenu;
    private String          settingsUrl;

    
    public LoginRespDTO() {
    }
    
    public LoginRespDTO(String login, String token, boolean isSuperUser) {
        this.login       = login;
        this.token       = token;
        this.superUser   = isSuperUser;
    }
    
    public LoginRespDTO(String login, String token, List<Integer> roles, 
            List<PermissionNodeDTO> permissions) {
        
        this.login       = login;
        this.token       = token;
        this.roles       = roles;
        this.permissions = permissions;
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
    
    public List<PermissionNodeDTO> safeGetPermissions() {
        if (permissions == null) {
            return Collections.emptyList();
        }
        
        return permissions;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetPermissions()
     */
    @Deprecated
    public List<PermissionNodeDTO> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<PermissionNodeDTO> permissions) {
        this.permissions = permissions;
    }

    public boolean safeIsSuperUser() {
        return (superUser != null ? superUser.booleanValue() : false);
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see safeIsSuperUser()
     */
    @Deprecated
    public Boolean isSuperUser() {
        return superUser;
    }
    
    public void setSuperUser(Boolean isSuperUser) {
        this.superUser = isSuperUser;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
    
    public AppMenuDTO getAppMenu() {
        return appMenu;
    }
    
    public void setAppMenu(AppMenuDTO appMenu) {
        this.appMenu = appMenu;
    }
    
    public String getSettingsUrl() {
        return settingsUrl;
    }
    
    public void setSettingsUrl(String settingsUrl) {
        this.settingsUrl = settingsUrl;
    }

}
