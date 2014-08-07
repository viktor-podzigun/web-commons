
package com.googlecode.common.protocol.dict;


/**
 * Common dictionary.
 */
public final class DictDTO {

    private int     id;
    private String  name;
    
    
    public DictDTO() {
    }

    public DictDTO(int id, String name) {
        this.id   = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
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
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DictDTO other = (DictDTO) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
