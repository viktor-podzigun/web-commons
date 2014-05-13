
package com.googlecode.common.service.impl;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.googlecode.common.service.ManageableService;
import com.googlecode.common.service.ServiceManager;


/**
 * Default implementation for ServiceManager interface.
 * 
 * @see ServiceManager
 */
@Service
@Singleton
@Lazy
public final class ServiceManagerImpl implements ServiceManager {

    private final Logger        log = LoggerFactory.getLogger(getClass());

    /** List of registered services */
    private final ArrayList<ManageableService> serviceList = 
        new ArrayList<ManageableService>();
    
    private final Object        lock = new Object();
    
    
    @PostConstruct
    public void init() {
        log.info("Initialization...");
    }
        
    @PreDestroy
    public void dispose() {
        log.info("Destroying...");
    }
    
    @Override
    public boolean registerService(ManageableService service) {
        if (service == null) {
            throw new NullPointerException("service");
        }
        
        synchronized (lock) {
            if (serviceList.indexOf(service) == -1) {
                log.info("Registering service: " + service);
                
                serviceList.add(service);
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void restartServices() {
        synchronized (lock) {
            for (int i = serviceList.size() - 1; i >= 0; i--) {
                ManageableService service = serviceList.get(i);
                service.restart();
            }
        }
    }

}
