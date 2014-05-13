
package com.googlecode.common.protocol.admin;

import java.util.Collections;
import java.util.List;


/**
 * Part of server responses, containing user authorization info.
 */
public final class AuthUserDTO {

    private int             userId;
    private String          login;
    private List<Integer>   roles;
    
    
    public AuthUserDTO() {
    }

    public AuthUserDTO(int userId, String login, List<Integer> roles) {
        this.userId = userId;
        this.login  = login;
        this.roles  = roles;
    }

    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
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
        return getClass().getName() + "{userId: " + userId
                + (login != null ? ", login: " + login : "")
                + "}";
    }
    
}
