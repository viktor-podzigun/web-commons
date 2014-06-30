
package com.googlecode.common.protocol;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Base class for permissions node.
 * 
 * <p>Contains list of permissions and can have child nodes.
 */
public class PermissionNode {

    private static final PermissionNode[]   emptyNodes       = new PermissionNode[0];
    private static final Permission[]       emptyPermissions = new Permission[0];
    
    /**
     * The list of permissions where the key is permission name 
     * and the data is <code>Permission</code> object.
     */
    private final List<Permission>          permissions;

    /**
     * The list of permissions nodes where the key is node's name 
     * and the data is <code>PermissionNode</code> object.
     */
    private final List<PermissionNode>      nodes;

    private final String                    name;
    private final String                    title;
    
    
    public PermissionNode(String name, String title, 
            List<Permission> permissions, PermissionNode... nodes) {
        
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is null or empty");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title is null or empty");
        }
        
        this.name   = name;
        this.title  = title;
        
        if (permissions == null || permissions.size() == 0) {
            this.permissions = Collections.emptyList();
        } else {
            this.permissions = permissions;
        }
        
        if (nodes == null || nodes.length == 0) {
            this.nodes = Collections.emptyList();
        } else {
            this.nodes = Arrays.asList(nodes);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return name;
    }

    public PermissionNode getNode(String name) {
        for (PermissionNode n : nodes) {
            if (n.getName().equals(name)) {
                return n;
            }
        }
        
        return null;
    }

    public Permission getPermission(String name) {
        for (Permission p : permissions) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        
        return null;
    }

    public PermissionNode[] getNodes() {
        return nodes.toArray(emptyNodes);
    }

    public Permission[] getPermissions() {
        return permissions.toArray(emptyPermissions);
    }

    protected static Permission add(List<Permission> permissionsList, 
            Permission permission) {

        if (permissionsList == null) {
            throw new NullPointerException("permissionsList");
        }
        if (permission == null) {
            throw new NullPointerException("permission");
        }
        
        permissionsList.add(permission);
        return permission;
    }
}
