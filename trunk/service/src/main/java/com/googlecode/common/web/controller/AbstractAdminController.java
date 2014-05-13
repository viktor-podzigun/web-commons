
package com.googlecode.common.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.login.LoginRedirectResponse;
import com.googlecode.common.protocol.login.LoginRequests;
import com.googlecode.common.protocol.login.LoginRespDTO;
import com.googlecode.common.protocol.login.LoginResponse;
import com.googlecode.common.service.AdminService;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.web.ServletHelpers;


/**
 * Contains common admin functionality.
 */
public abstract class AbstractAdminController extends AbstractServerController {
    
    @Autowired
    private AdminService    adminService;
    

    @RequestMapping(value   = LoginRequests.LOGIN,
                    method  = RequestMethod.GET)
    public @ResponseBody LoginResponse login(
            HttpServletRequest req, HttpServletResponse resp, 
            @RequestParam(value="rm", defaultValue="false") boolean rm) {
        
        String authInfo[] = ServletHelpers.getBasicAuthInfo(req);
        if (authInfo == null || authInfo.length != 2) {
            throw new OperationFailedException(
                    CommonResponses.AUTHENTICATION_FAILED, 
                    "User authentication failure");
        }
        
        LoginResponse loginResp = adminService.loginUser(
                authInfo[0], authInfo[1], rm);
        
        LoginRespDTO loginDto = loginResp.getData();
        if (loginDto != null) {
            ServletHelpers.addTokenCookie(resp, loginDto.getToken());
        }
        
        return loginResp;
    }
    
    @RequestMapping(value   = LoginRequests.LOGIN_TOKEN,
                    method  = RequestMethod.GET)
    public @ResponseBody LoginResponse loginToken(
            HttpServletRequest req, HttpServletResponse resp) {
        
        LoginResponse loginResp = adminService.loginToken(
                ServletHelpers.getAuthToken(req));
        
        LoginRespDTO loginDto = loginResp.getData();
        if (loginDto != null) {
            ServletHelpers.addTokenCookie(resp, loginDto.getToken());
        }
        
        return loginResp;
    }

    @RequestMapping(value   = LoginRequests.LOGOUT,
                    method  = RequestMethod.GET)
    public @ResponseBody BaseResponse logout(HttpServletRequest req, 
            HttpServletResponse resp) {
        
        adminService.logoutUser(ServletHelpers.getAuthToken(req));
        
        ServletHelpers.removeTokenCookie(resp);
        
        // put login redirect URL only if in the same domain
        if (req.getServerName().endsWith(ServletHelpers.getAppDomain())) {
            return new LoginRedirectResponse(
                    adminService.getLoginRedirectUrl(req, null));
        }
        
        return BaseResponse.OK;
    }

}
