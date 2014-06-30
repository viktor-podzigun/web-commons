
package com.googlecode.common.protocol.admin;


/**
 * Part of server responses, containing server module info.
 */
public final class ServerModuleDTO {

    private String      title;
    private String      author;
    private String      vendor;
    private String      version;
    private String      build;

    
    public ServerModuleDTO() {
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getVendor() {
        return vendor;
    }
    
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getBuild() {
        return build;
    }
    
    public void setBuild(String build) {
        this.build = build;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{title: " + title
                + (author != null ? ", author: " + author : "")
                + (vendor != null ? ", vendor: " + vendor : "")
                + (version != null ? ", version: " + version : "")
                + (build != null ? ", build: " + build : "") 
                + "}";
    }

}
