
package com.googlecode.common.protocol.login;


/**
 * Contains login requests' path constants.
 */
public final class LoginRequests {

    public static final String  LOGIN       = "/login";
    public static final String  LOGIN_TOKEN = "/login/token";
    public static final String  LOGOUT      = "/logout";

    
    private LoginRequests() {
    }
    
    
    public static String login(boolean rm) {
        return LOGIN + "?rm=" + rm;
    }
    
}
