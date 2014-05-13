
package com.googlecode.common.protocol.admin;

import com.googlecode.common.protocol.RequestBuilder;
import com.googlecode.common.protocol.Requests;


/**
 * Common admin requests.
 */
public class AdminRequests extends Requests {

    public static final String  GET_STATUS          = "/admin/status";
    public static final String  GET_MODULES         = "/admin/modules";
    public static final String  RESTART             = "/admin/restart";
    
    public static final String  APP_READ_SETTINGS   = "/system/settings";
    public static final String  APP_LOGIN           = "/system/{system}/login";
    public static final String  APP_LOGIN_TOKEN     = "/system/{system}/login/token";
    public static final String  APP_AUTH_USER       = "/system/{system}/auth";

    
    // Requests parameters
    
    public static final String  PARAM_SYSTEM        = "system";

    
    public static String appLogin(String system, boolean rememberMe) {
        RequestBuilder builder = new RequestBuilder(APP_LOGIN);
        builder.setParam(PARAM_SYSTEM, system);
        String req = builder.build();
        if (rememberMe) {
            return req + "?rm=true";
        }
        
        return req;
    }

    public static String appLoginToken(String system) {
        RequestBuilder builder = new RequestBuilder(APP_LOGIN_TOKEN);
        builder.setParam(PARAM_SYSTEM, system);
        return builder.build();
    }

    public static String appAuthUser(String system) {
        RequestBuilder builder = new RequestBuilder(APP_AUTH_USER);
        builder.setParam(PARAM_SYSTEM, system);
        return builder.build();
    }

}
