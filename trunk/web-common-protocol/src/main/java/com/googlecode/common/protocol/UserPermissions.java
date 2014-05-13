
package com.googlecode.common.protocol;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.googlecode.common.protocol.perm.PermissionDTO;
import com.googlecode.common.protocol.perm.PermissionNodeDTO;


/**
 * Contains user permissions data.
 */
public final class UserPermissions {
    
    private static volatile UserPermissions instance;
    
    private final boolean           isSuperUser;
    private final Set<Permission>   permissions;

    
    private UserPermissions(boolean isSuperUser, PermissionNode root, 
            List<PermissionNodeDTO> permList) {
        
        this.isSuperUser = isSuperUser;
        
        if (!isSuperUser) {
            permissions = new HashSet<Permission>();
            
            for (PermissionNodeDTO dto : permList) {
                loadNode(root, dto/*, ""*/);
            }
        } else {
            permissions = null;
        }
    }
    
    private void loadNode(PermissionNode parent, 
            PermissionNodeDTO dto/*, String absolutePath*/) {

        String name = dto.getName();
//        absolutePath += "/" + name;
        
        PermissionNode node = parent.getNode(name);
        if (node == null) {
            // permission node not found: absolutePath
            return;
        }
        
        for (PermissionNodeDTO n : dto.safeGetNodes()) {
            loadNode(node, n/*, absolutePath*/);
        }
        
        for (PermissionDTO p : dto.safeGetPermissions()) {
            loadPermission(node, p/*, absolutePath*/);
        }
    }
    
    private void loadPermission(PermissionNode parent, PermissionDTO dto
            /*, String absolutePath*/) {
        
        String name = dto.getName();
//        absolutePath += "/" + name;
        
        Permission p = parent.getPermission(name);
        if (p == null) {
            // permission not found: absolutePath
            return;
        }
     
        permissions.add(p);
    }
    
    private boolean hasPermission(Permission p) {
        return (isSuperUser ? true : permissions.contains(p));
    }
    
    private static UserPermissions getCheckInstance() {
        UserPermissions permiss = instance;
        if (permiss == null) {
            throw new IllegalStateException("No instance was created. "
                    + "Did you forget to call UserPermissions.apply() ?");
        }
        
        return permiss;
    }

    public static void apply(boolean isSuperUser, PermissionNode root, 
            List<PermissionNodeDTO> permList) {
        
        instance = new UserPermissions(isSuperUser, root, permList);
    }
    
    public static boolean has(Permission p) {
        return getCheckInstance().hasPermission(p);
    }
    
    public static boolean hasAny(Permission... permissions) {
        UserPermissions permiss = getCheckInstance();
        for (Permission p : permissions) {
            if (permiss.hasPermission(p)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isSuperUser() {
        return getCheckInstance().isSuperUser;
    }

}
