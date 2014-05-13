
package com.googlecode.common.service;


/**
 * Contains service managements methods.
 */
public interface ServiceManager {

    public boolean registerService(ManageableService service);
    
    public void restartServices();

}
