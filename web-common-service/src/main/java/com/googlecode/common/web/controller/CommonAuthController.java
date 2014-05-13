
package com.googlecode.common.web.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.googlecode.common.protocol.Permission;
import com.googlecode.common.protocol.admin.AuthUserDTO;
import com.googlecode.common.service.AdminService;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.PermissionService;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.web.ServletHelpers;


/**
 * Common admin authorization controller.
 */
public abstract class CommonAuthController {

    protected final Logger      log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private AdminService        adminService;
    
    @Autowired
    private PermissionService   permissionService;
    
    
    /**
     * Returns authorization info for the given request.
     * 
     * @param request   request object
     * @return          authorization info
     * 
     * @throws OperationFailedException if no authorization info provided
     */
    protected String[] getAuthInfo(HttpServletRequest request) {
        String[] authInfo = ServletHelpers.getBasicAuthInfo(request);
        if (authInfo == null || authInfo.length != 2) {
            if (log.isWarnEnabled()) {
                log.warn("No authorization data provided");
            }
        
            throw new OperationFailedException(
                    CommonResponses.AUTHENTICATION_FAILED,
                    "Authentication failed");
        }
        
        return authInfo;
    }
    
    /**
     * Reads user's roles for the given request.
     * 
     * @param request   request object
     * @return          user's roles
     * 
     * @throws OperationFailedException if no authorization info provided
     */
    protected AuthUserDTO authUser(HttpServletRequest request) {
        return adminService.authUser(ServletHelpers.getAuthToken(request));
    }
    
    /**
     * Checks if the specified user has the specified permission.
     * 
     * @param user      user's roles
     * @param p         permission to check
     * 
     * @throws OperationFailedException if the specified user doesn't have 
     *              the specified permission
     * 
     * @see #checkUserAnyPermissions(AuthUserDTO, Permission...)
     * @see #hasUserPermission(AuthUserDTO, Permission)
     */
    protected void checkUserPermission(AuthUserDTO user, Permission p) {
        if (!hasUserPermission(user, p)) {
            throw new OperationFailedException(CommonResponses.ACCESS_DENIED, 
                    "Access denied for specified entity");
        }
    }

    /**
     * Checks if the specified user has at least on of the specified 
     * permission(s).
     * 
     * @param user          user's roles
     * @param permissions   permission(s) to check
     * 
     * @throws OperationFailedException if the specified user doesn't have 
     *              the specified permission
     * 
     * @see #checkUserAllPermissions(AuthUserDTO, Permission...)
     * @see #checkUserPermission(AuthUserDTO, Permission)
     * @see #hasUserPermission(AuthUserDTO, Permission)
     */
    protected void checkUserAnyPermissions(AuthUserDTO user, 
            Permission... permissions) {
        
        List<Integer> roles = user.safeGetRoles();
        for (Permission p : permissions) {
            if (permissionService.hasRolePermission(roles, p)) {
                return;
            }
        }
        
        throw new OperationFailedException(CommonResponses.ACCESS_DENIED, 
                "Access denied for specified entity");
    }

    /**
     * Checks if the specified user has all of the specified permissions.
     * 
     * @param user          user's roles
     * @param permissions   permissions to check
     * 
     * @throws OperationFailedException if the specified user doesn't have 
     *              the specified permissions
     * 
     * @see #checkUserAnyPermissions(AuthUserDTO, Permission...)
     * @see #checkUserPermission(AuthUserDTO, Permission)
     * @see #hasUserPermission(AuthUserDTO, Permission)
     */
    protected void checkUserAllPermissions(AuthUserDTO user, Permission p1, 
            Permission p2) {
        
        checkUserPermission(user, p1);
        checkUserPermission(user, p2);
    }

    /**
     * Checks if the specified user has the specified permission.
     * 
     * @param user      user's roles
     * @param p         permission to check
     * @return          <code>true</code> if the specified user has the 
     *                  specified permission, or <code>false</code> otherwise
     * 
     * @see #checkUserPermission(AuthUserDTO, Permission)
     * @see #checkUserAnyPermissions(AuthUserDTO, Permission...)
     */
    protected boolean hasUserPermission(AuthUserDTO user, Permission p) {
        return permissionService.hasRolePermission(user.safeGetRoles(), p);
    }
    
}
