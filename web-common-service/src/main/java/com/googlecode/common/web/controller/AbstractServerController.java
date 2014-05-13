
package com.googlecode.common.web.controller;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.googlecode.common.protocol.admin.AdminRequests;
import com.googlecode.common.protocol.admin.ServerModulesResponse;
import com.googlecode.common.protocol.admin.ServerStatusResponse;
import com.googlecode.common.service.ServerManager;
import com.googlecode.common.web.ServletHelpers;


/**
 * Contains common server functionality.
 */
public abstract class AbstractServerController {

    protected final Logger  log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ServerManager serverManager;

    
    @RequestMapping(value   = AdminRequests.GET_STATUS,
                    method  = RequestMethod.GET)
    public @ResponseBody ServerStatusResponse getStatus(
            HttpServletRequest req) {
        
        return new ServerStatusResponse(serverManager.getStatus(
                req.getSession().getServletContext()));
    }
    
    @RequestMapping(value   = AdminRequests.GET_MODULES,
                    method  = RequestMethod.GET)
    public @ResponseBody ServerModulesResponse getModules() {
        return new ServerModulesResponse(serverManager.getModules());
    }
    
    @RequestMapping(value   = AdminRequests.RESTART,
                    method  = RequestMethod.GET)
    public @ResponseBody ServerStatusResponse restart(
            HttpServletRequest req) {
        
        return new ServerStatusResponse(serverManager.restart(
                req.getSession().getServletContext(), getUserLogin(req)));
    }
    
    private String getUserLogin(HttpServletRequest request) {
        String[] authInfo = ServletHelpers.getBasicAuthInfo(request);
        if (authInfo != null) {
            return authInfo[0];
        }
        
        return null;
    }

}
