
package com.googlecode.common.service;

import java.util.List;
import javax.servlet.ServletContext;
import com.googlecode.common.protocol.admin.ServerModuleDTO;
import com.googlecode.common.protocol.admin.ServerStatusDTO;


/**
 * Contains statistics info of the server.
 */
public interface ServerManager {

    public void requestStarted();
    
    public void requestFinished(boolean success);
    
    public ServerStatusDTO getStatus(ServletContext servletContext);
    
    public List<ServerModuleDTO> getModules();
    
    public ServerStatusDTO restart(ServletContext servletContext, 
            String authorLogin);
    
}
