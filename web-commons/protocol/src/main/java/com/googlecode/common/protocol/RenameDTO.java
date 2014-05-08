
package com.googlecode.common.protocol;


/**
 * Common DTO for rename requests.
 */
public final class RenameDTO {

    private long    id;
    private String  name;
    
    
    public RenameDTO() {
    }

    public RenameDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{id: " + id
                + (name != null ? ", name: " + name : "")
                + "}";
    }
}
