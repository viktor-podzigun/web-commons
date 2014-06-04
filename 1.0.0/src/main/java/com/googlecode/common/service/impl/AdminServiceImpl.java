
package com.googlecode.common.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.googlecode.common.http.RequestException;
import com.googlecode.common.http.RequestParams;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.admin.AdminRequests;
import com.googlecode.common.protocol.admin.AuthUserDTO;
import com.googlecode.common.protocol.admin.AuthUserResponse;
import com.googlecode.common.protocol.login.LoginRequests;
import com.googlecode.common.protocol.login.LoginResponse;
import com.googlecode.common.service.AdminService;
import com.googlecode.common.service.AdminSettingsService;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.JsonRequestService;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.util.UriHelpers;
import com.googlecode.common.web.ServletHelpers;


/**
 * Default implementation for {@link AdminService} interface.
 */
@Service
@Singleton
@Lazy
public class AdminServiceImpl implements AdminService {
    
    private final Logger            log = LoggerFactory.getLogger(getClass());

    @Autowired(required=false)
    private AdminSettingsService    settingsService;
    
    @Autowired
    private JsonRequestService      requestClient;
    
    private String                  systemName;
    private String                  adminServerUrl;
    
    
    @PostConstruct
    public void init() {
        if (settingsService != null) {
            URI adminUrl = settingsService.getAdminServerUrl();
            log.info("adminServerUrl: " + UriHelpers.hidePassword(adminUrl));
            
            String[] info = UriHelpers.splitUserInfo(adminUrl.getUserInfo());
            
            systemName     = info[0];
            adminServerUrl = UriHelpers.hideUserInfo(adminUrl);
        }
    }
    
    private void checkAdminEnabled() {
        if (settingsService == null) {
            throw new OperationFailedException(
                    CommonResponses.ACCESS_DENIED, 
                    "Admin service is not enabled");
        }
    }
    
    @Override
    public LoginResponse loginUser(String userName, String userPass, 
            boolean rememberMe) {
        
        checkAdminEnabled();
        
        try {
            RequestParams params = new RequestParams();
            params.setUserCredentials(userName, userPass);
            params.setAcceptLanguage(ServletHelpers.getRequestLocale());
            
            return requestClient.read(params, adminServerUrl 
                    + AdminRequests.appLogin(systemName, rememberMe), 
                    LoginResponse.class);
        
        } catch (RequestException x) {
            throw new OperationFailedException(
                    CommonResponses.AUTHENTICATION_FAILED, 
                    "Failed to login user: " + userName
                        + ", status: " + x.getStatus() 
                        + ", message: " + x.getStatusText());
            
        } catch (IOException x) {
            throw new OperationFailedException(
                    CommonResponses.INTERNAL_SERVER_ERROR, 
                    "Failed to login user: " + userName, x);
        }
    }
    
    @Override
    public LoginResponse loginToken(String token) {
        checkAdminEnabled();
        
        try {
            RequestParams params = new RequestParams();
            params.setAcceptLanguage(ServletHelpers.getRequestLocale());
            params.setUserToken(token);
            
            return requestClient.read(params, adminServerUrl 
                    + AdminRequests.appLoginToken(systemName), 
                    LoginResponse.class);
        
        } catch (RequestException x) {
            throw new OperationFailedException(
                    CommonResponses.AUTHENTICATION_FAILED, 
                    "Failed to login by token: " + token
                        + ", status: " + x.getStatus() 
                        + ", message: " + x.getStatusText());
            
        } catch (IOException x) {
            throw new OperationFailedException(
                    CommonResponses.INTERNAL_SERVER_ERROR, 
                    "Failed to login by token: " + token, x);
        }
    }
    
    @Override
    public void logoutUser(String token) {
        checkAdminEnabled();
        
        try {
            RequestParams params = new RequestParams();
            params.setAcceptLanguage(ServletHelpers.getRequestLocale());
            params.setUserToken(token);
            
            BaseResponse resp = requestClient.read(params, adminServerUrl 
                    + LoginRequests.LOGOUT, BaseResponse.class);
            
            if (resp.getStatus() != BaseResponse.OK_STATUS) {
                log.warn("Logout failed, token: " + token 
                        + ", status: " + resp.getStatus() 
                        + ", message: " + resp.getMessage());
            }
        } catch (IOException x) {
            log.warn("Logout failed, token: " + token + ", error: " + x);
        }
    }
    
    @Override
    public AuthUserDTO authUser(String token) {
        checkAdminEnabled();
        
        try {
            RequestParams params = new RequestParams();
            params.setAcceptLanguage(ServletHelpers.getRequestLocale());
            params.setUserToken(token);
            
            AuthUserResponse resp = requestClient.read(params, 
                    adminServerUrl + AdminRequests.appAuthUser(systemName), 
                    AuthUserResponse.class);
            
            String error;
            if (resp.getStatus() == BaseResponse.OK_STATUS) {
                AuthUserDTO dto = resp.getData();
                if (dto != null) {
                    return dto;
                }
                
                error = "No response data";
            } else {
                error = resp.getMessage();
            }
            
            throw new OperationFailedException(
                    CommonResponses.INTERNAL_SERVER_ERROR, 
                    "Failed to authorize user"
                        + ", status: " + resp.getStatus()
                        + ", error: " + error);
            
        } catch (RequestException x) {
            throw new OperationFailedException(
                    CommonResponses.AUTHENTICATION_FAILED, 
                    "Failed to authorize user"
                        + ", status: " + x.getStatus() 
                        + ", message: " + x.getStatusText());
            
        } catch (IOException x) {
            throw new OperationFailedException(
                    CommonResponses.INTERNAL_SERVER_ERROR, 
                    "Failed to authorize user", x);
        }
    }
    
    @Override
    public String getLoginRedirectUrl(HttpServletRequest req, 
            String targetUrl) {
        
        StringBuilder sb = new StringBuilder(512);
        if (settingsService != null) {
            sb.append(adminServerUrl);
        } else {
            // if we on the admin server then get URL from the request
            URI url = URI.create(ServletHelpers.getRequestUrl(req));
            sb.append(UriHelpers.setPath(url, "/admin"));
        }
        
        sb.append("/signin.html");
        
        if (targetUrl != null) {
            try {
                sb.append("?continue=")
                    .append(URLEncoder.encode(targetUrl, "UTF-8"));
            
            } catch (UnsupportedEncodingException x) {
                // should never happen
                log.error(null, x);
            }
        }
        
        return sb.toString();
    }

}
