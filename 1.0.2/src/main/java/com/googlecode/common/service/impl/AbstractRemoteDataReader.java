
package com.googlecode.common.service.impl;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract remote data reader.
 */
public abstract class AbstractRemoteDataReader {

    private final Logger            log = LoggerFactory.getLogger(getClass());
    private final ExecutorService   executor = Executors.newCachedThreadPool();
    
    
    protected AbstractRemoteDataReader() {
    }
    
    public void execute() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    doRead();
                
                } catch (Exception x) {
                    log.error("RemoteDataReader", x);
                }
            }
        });
    }
    
    public void shutdown() {
        try {
            executor.shutdownNow();
            executor.awaitTermination(20, TimeUnit.SECONDS);
        
        } catch (InterruptedException x) {
            throw new RuntimeException(x);
        }
    }
    
    protected abstract void readData() throws IOException;
    
    protected abstract void dataReady();
    
    private void doRead() {
        try {
            while (true) {
                try {
                    Thread.sleep(5000);
                    readData();
                    break;
                    
                } catch (IOException x) {
                    log.warn("Failed to read data"
                            + ", retry in 5 sec, error: " + x);
                }
            }
            
            dataReady();
            
        } catch (InterruptedException x) {
            // we expect this
        }
    }

}
