
package com.googlecode.common.service.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.googlecode.common.service.ManageableService;
import com.googlecode.common.service.ServiceManager;


/**
 * Contains common functionality for manageable services.
 */
public abstract class AbstractManageableService implements ManageableService {

    protected final Logger          log = LoggerFactory.getLogger(getClass());
    
    @Autowired(required = false)
    protected ServiceManager        serviceManager;
    
    private final ReadWriteLock     readWriteLock   = new ReentrantReadWriteLock();
    protected final Lock            readLock        = readWriteLock.readLock();
    protected final Lock            writeLock       = readWriteLock.writeLock();

    
    protected void init() {
        log.info("Initialization...");
        
        if (serviceManager != null) {
            serviceManager.registerService(this);
        }
    }
    
    protected void destroy() {
        log.info("Destroying...");
    }

    @Override
    public void restart() {
        log.info("Restarting...");
        
        writeLock.lock();
        try {
            destroy();
            init();
            
        } finally {
            writeLock.unlock();
        }
    }
    
}
