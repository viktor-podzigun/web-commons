
package com.googlecode.common.service;

import java.net.URI;
import java.util.List;


/**
 * Provides admin server specific settings.
 */
public interface AdminSettingsService {

    /**
     * Returns admin server url.
     * @return admin server url
     */
    public URI getAdminServerUrl();
    
    /**
     * Checks system authentication info.
     * 
     * @param name  system name
     * @param pass  system password
     * @return      <code>true</code> if, and only if, such system exists and 
     *              password matches or <code>false</code> otherwise
     */
    public boolean authSystem(String name, String pass);
    
    /**
     * Returns system title by the given unique name.
     * 
     * @param name  system unique name
     * @return      system title
     */
    public String getSystemTitle(String name);
    
    /**
     * Returns registered systems (applications) names.
     * @return registered systems (applications) names
     */
    public List<String> getSystems();
    
}
