
package com.googlecode.common.service;

import java.io.IOException;


/**
 * Contains useful methods for read/write web server settings.
 */
public interface SettingsService {

    /**
     * Returns current server settings
     * @return current server settings
     */
//    public ConfigSettings getServerSettings();
    
    /**
     * Refreshes current settings cache.
     */
    public void reloadSettings() throws IOException;
    
}
