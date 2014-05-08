
package com.googlecode.common.service;

import java.util.List;
import com.googlecode.common.protocol.Permission;


/**
 * Contains methods for working with user's permissions.
 */
public interface PermissionService {

    /**
     * Checks if any of the specified roles has the specified permission.
     * 
     * @param roles roles bit set
     * @param p     permission to check
     * @return      <code>true</code> if any of the specified roles has 
     *              the specified permission, or <code>false</code> otherwise
     */
    public boolean hasRolePermission(List<Integer> roles, Permission p);
    
}
