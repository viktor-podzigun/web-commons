
package com.googlecode.common.service;

import javax.servlet.http.HttpServletRequest;
import com.googlecode.common.protocol.admin.AuthUserDTO;
import com.googlecode.common.protocol.login.LoginResponse;


/**
 * Provides admin service methods.
 */
public interface AdminService {

    /**
     * Authenticate and login the given user.
     * 
     * @param userName      user name
     * @param userPass      user password
     * @param rememberMe    indicates whether to create rememberMe token
     * @return              login response
     */
    public LoginResponse loginUser(String userName, String userPass, 
            boolean rememberMe);
    
    /**
     * Authenticate and login the given user by the given token.
     * 
     * @param token         user token
     * @return              login response
     */
    public LoginResponse loginToken(String token);
    
    /**
     * Log-outs user by the given token.
     * 
     * @param token     user token
     */
    public void logoutUser(String token);
    
    /**
     * Reads user's authorization info by the given token.
     * 
     * @param token     user's token
     * @return          user's authorization info
     */
    public AuthUserDTO authUser(String token);
    
    /**
     * Returns login page redirect URL.
     * 
     * @param req       current HTTP request
     * @param targetUrl URL to redirect to after login success
     * @return          login page redirect URL
     */
    public String getLoginRedirectUrl(HttpServletRequest req, String targetUrl);
    
}
