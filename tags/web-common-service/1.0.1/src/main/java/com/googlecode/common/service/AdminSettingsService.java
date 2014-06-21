
package com.googlecode.common.service;

import java.net.URI;


/**
 * Provides admin server specific settings.
 */
public interface AdminSettingsService {

    /**
     * Returns admin server url.
     * @return admin server url
     */
    public URI getAdminServerUrl();
    
}
