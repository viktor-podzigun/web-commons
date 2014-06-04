
package com.googlecode.common.protocol;


/**
 * Contains permission definition parameters.
 */
public final class Permission {

    private final String        name;
    private final String        title;
    
    
    private Permission(String name, String title) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is null or empty");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title is null or empty");
        }
        
        this.name  = name;
        this.title = title;
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

    public static Permission newPerm(String name, String title) {
        return new Permission(name, title);
    }
    
    public static Permission newReadPerm() {
        return new Permission("read", "Read");
    }

    public static Permission newCreatePerm() {
        return new Permission("create", "Create");
    }

    public static Permission newUpdatePerm() {
        return new Permission("update", "Update");
    }

    public static Permission newRenamePerm() {
        return new Permission("rename", "Rename");
    }

    public static Permission newDeletePerm() {
        return new Permission("delete", "Delete");
    }

    public static Permission newPrintPerm() {
        return new Permission("print", "Print");
    }
}
