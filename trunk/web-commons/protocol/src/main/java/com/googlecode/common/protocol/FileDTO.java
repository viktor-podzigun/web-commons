
package com.googlecode.common.protocol;


/**
 * Represents file data info.
 */
public final class FileDTO {

    private String  name;
    private byte[]  data;

    
    public FileDTO() {
    }
    
    public FileDTO(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "name: " + name;
    }

}
