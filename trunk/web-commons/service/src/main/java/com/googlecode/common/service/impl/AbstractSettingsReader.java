
package com.googlecode.common.service.impl;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.googlecode.common.service.SettingsService;


/**
 * Runnable task for reading remote settings.
 * 
 * @see AbstractRemoteSettings
 */
public abstract class AbstractSettingsReader implements Runnable {

    protected final Logger          log = LoggerFactory.getLogger(getClass());
    
    protected final SettingsService settingsService;
    
    
    protected AbstractSettingsReader(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
    
    protected abstract void settingsReady();
    
    @Override
    public void run() {
        try {
            while (true) {
                try {
                    Thread.sleep(5000);
                    settingsService.reloadSettings();
                    break;
                    
                } catch (IOException x) {
                    log.warn("Failed to read settings, "
                            + "retry in 5 sec, error: " + x);
                }
            }
            
            settingsReady();
            
        } catch (InterruptedException x) {
            // we expect this
        
        } catch (Exception x) {
            log.error("SettingsReader", x);
        }
    }
}
