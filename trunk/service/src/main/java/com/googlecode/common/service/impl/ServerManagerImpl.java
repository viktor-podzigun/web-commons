
package com.googlecode.common.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.jar.Manifest;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.googlecode.common.protocol.admin.ServerModuleDTO;
import com.googlecode.common.protocol.admin.ServerStatusDTO;
import com.googlecode.common.service.ServerManager;
import com.googlecode.common.service.ServiceManager;
import com.googlecode.common.service.SettingsService;
import com.googlecode.common.util.ModuleInfo;


/**
 * Default implementation for {@link ServerManager} interface.
 */
@Service("serverManager")
@Singleton
@Lazy
public class ServerManagerImpl implements ServerManager {

    private final Logger        log = LoggerFactory.getLogger(getClass());
    
    @Autowired(required = false)
    private SettingsService         settingsService;
    
    @Autowired
    private ServiceManager          serviceManager;
    
    private final Date              startDate    = new Date();
    private final AtomicInteger     restartCount = new AtomicInteger();
    
    private volatile Statistics     statistics   = new Statistics();
    private volatile ModuleInfo     appModule;
    private volatile ModuleInfo[]   gmModules;
    
    
    @Override
    public void requestStarted() {
        statistics.requestStarted();
    }
    
    @Override
    public void requestFinished(boolean success) {
        statistics.requestFinished(success);
    }
    
    @Override
    public List<ServerModuleDTO> getModules() {
        if (gmModules == null) {
            gmModules = ModuleInfo.readAllByVendor(getClass().getClassLoader(), 
                    "Win Interactive");
        }
        
        List<ServerModuleDTO> list = new ArrayList<ServerModuleDTO>(
                gmModules.length);
        
        for (ModuleInfo m : gmModules) {
            ServerModuleDTO dto = new ServerModuleDTO();
            dto.setTitle(m.getTitle());
            dto.setVersion(m.getVersion());
            dto.setBuild(m.getBuild());
            dto.setAuthor(m.getAuthor());
            list.add(dto);
        }
        
        return list;
    }
    
    @Override
    public ServerStatusDTO getStatus(ServletContext servletContext) {
        // read version info
        if (appModule == null) {
            try {
                appModule = ModuleInfo.readFromManifest(new Manifest(
                        servletContext.getResourceAsStream(
                                "/WEB-INF/classes/META-INF/MANIFEST.MF")));
            
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }
        
        ServerStatusDTO dto = new ServerStatusDTO();
        dto.setStartDate(startDate);
        dto.setAppVersion(appModule.getVersion());
        dto.setAppBuild(appModule.getBuild());
        
        dto.setSucceededReqCount(statistics.getSucceededRequests());
        dto.setFailedReqCount(statistics.getFailedRequests());
        dto.setActiveReqCount(statistics.getActiveRequests());
        dto.setMaxActiveReqCount(statistics.getMaxActiveRequests());
        
        dto.setRestartAuthorLogin(statistics.getRestartAuthor());
        dto.setRestartDate(statistics.getRestartDate());
        dto.setRestartCount(restartCount.get());
        return dto;
    }

    @Override
    public ServerStatusDTO restart(ServletContext servletContext, 
            String authorLogin) {
        
        log.info("Restarting server by: " + authorLogin);
        
        restartCount.incrementAndGet();
        statistics = new Statistics(authorLogin, new Date());

        try {
            if (settingsService != null) {
                settingsService.reloadSettings();
            }
        } catch (IOException x) {
            throw new RuntimeException("Failed to reload server settings", x);
        }
            
        serviceManager.restartServices();
        return getStatus(servletContext);
    }

    
    /**
     * Contains server access statistics.
     */
    private static final class Statistics {
        
        private final String        restartAuthor;
        private final Date          restartDate;
        
        private final AtomicLong    succeededRequests   = new AtomicLong();
        private final AtomicLong    failedRequests      = new AtomicLong();
        private final AtomicInteger activeRequests      = new AtomicInteger();
        private final AtomicInteger maxActiveRequests   = new AtomicInteger();
        
        
        public Statistics() {
            this(null, null);
        }
        
        public Statistics(String restartAuthor, Date restartDate) {
            this.restartAuthor = restartAuthor;
            this.restartDate = restartDate;
        }
        
        public String getRestartAuthor() {
            return restartAuthor;
        }
        
        public Date getRestartDate() {
            return restartDate;
        }

        public long getSucceededRequests() {
            return succeededRequests.get();
        }
        
        public long getFailedRequests() {
            return failedRequests.get();
        }
        
        public int getActiveRequests() {
            return activeRequests.get();
        }
        
        public int getMaxActiveRequests() {
            return maxActiveRequests.get();
        }
    
        public void requestStarted() {
            int curr = activeRequests.incrementAndGet();
            
            // atomically update maxActiveRequests
            for (;;) {
                int max = maxActiveRequests.get();
                if (max < curr) {
                    if (maxActiveRequests.compareAndSet(max, curr)) {
                        return;
                    }
                } else {
                    return;
                }
            }
        }
        
        public void requestFinished(boolean success) {
            activeRequests.decrementAndGet();
            if (success) {
                succeededRequests.incrementAndGet();
            } else {
                failedRequests.incrementAndGet();
            }
        }
    }
    
}
