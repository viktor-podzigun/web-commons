
package com.googlecode.common.protocol.admin;


/**
 * Part of server responses containing application system info.
 */
public final class AppSystemDTO {

    private String  name;
    private String  passHash; // SHA-1 password hash
    private String  title;
    
    
    public AppSystemDTO() {
    }
    
    public AppSystemDTO(String name, String passHash, String title) {
        this.name     = name;
        this.passHash = passHash;
        this.title    = title;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassHash() {
        return passHash;
    }
    
    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

}
