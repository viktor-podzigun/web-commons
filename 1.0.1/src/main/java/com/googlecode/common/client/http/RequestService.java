
package com.googlecode.common.client.http;

import java.util.Date;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.googlecode.common.client.task.AbstractTask;


/**
 * Service for performing HTTP requests.
 */
public final class RequestService {
    
    public static final RequestService  INSTANCE = new RequestService();

    private String                  baseUrl = computeBaseURL(null);
    
    private RequestErrorHandler     errorHandler;
    private String                  userLogin;
    
    
    private static String computeBaseURL(String moduleName) {
        String base = GWT.getModuleBaseURL();
        
        // truncate module name from URL
        final int nameIndex = base.lastIndexOf(GWT.getModuleName());
        if (nameIndex != -1) {
            base = base.substring(0, nameIndex);
        }
        
        if (!GWT.isProdMode()) {
            // used by ProxyServlet in Development mode
            base = base + "proxy/" + (moduleName != null ? 
                    moduleName : GWT.getModuleName());
        }
        
        return base;
    }
    
    public void setModuleBaseUrl(String moduleName) {
        baseUrl = computeBaseURL(moduleName);
    }
    
    public static <T extends RestService> T prepare(T service) {
        RestServiceProxy proxy = (RestServiceProxy)service;
        proxy.setResource(resource(""));
        return service;
    }
    
    public static Resource resource(String url) {
        Resource res = new Resource(INSTANCE.getURL() + url);
        res = res.addQueryParam("_t", String.valueOf(new Date().getTime()));
        return res;
    }
    
    public void setErrorHandler(RequestErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
    
    protected boolean handleErrorStatus(AbstractTask task, Response response) {
        if (errorHandler != null) {
            return errorHandler.handleErrorStatus(task, response);
        }
        
        return false;
    }

    public String getURL() {
        return baseUrl;
    }

    public String getUserLogin() {
        return userLogin;
    }
    
    public void setUserLogin(String login) {
        this.userLogin = login;
    }
    
}
