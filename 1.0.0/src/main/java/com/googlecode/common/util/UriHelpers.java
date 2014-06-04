
package com.googlecode.common.util;

import java.net.URI;
import java.util.regex.Pattern;


/**
 * Contains URI helpers methods.
 */
public final class UriHelpers {

    private static final Pattern    USER_INFO_SPLIT = Pattern.compile(":");
    
    private UriHelpers() {
    }
    
    /**
     * Splits the given user info in the format user:password to separate 
     * strings.
     * 
     * @param userInfo  input user info
     * @return          array of two separate strings: [0]=user, [1]=password
     */
    public static String[] splitUserInfo(String userInfo) {
        return USER_INFO_SPLIT.split(userInfo, 2);
    }
    
    public static String hidePassword(URI uri) {
        int port = uri.getPort();
        String path = uri.getPath();
        
        String portStr = (port > 0 ? ":" + port : "");
        String pathStr = (path != null ? path : "");

        String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            userInfo = splitUserInfo(userInfo)[0] + "@";
        } else {
            userInfo = "";
        }
        
        return uri.getScheme() + "://" + userInfo + uri.getHost() + portStr 
                + pathStr;
    }

    public static String hideUserInfo(URI uri) {
        int port = uri.getPort();
        String path = uri.getPath();
        
        String portStr = (port > 0 ? ":" + port : "");
        String pathStr = (path != null ? path : "");

        return uri.getScheme() + "://" + uri.getHost() + portStr + pathStr;
    }
    
    public static String setHost(URI uri, String newHost) {
        int port = uri.getPort();
        String path = uri.getPath();
        
        String portStr = (port > 0 ? ":" + port : "");
        String pathStr = (path != null ? path : "");

        String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            userInfo = userInfo + "@";
        } else {
            userInfo = "";
        }
        
        return uri.getScheme() + "://" + userInfo + newHost + portStr 
                + pathStr;
    }
    
    public static String setPath(URI uri, String newPath) {
        int    port    = uri.getPort();
        String portStr = (port > 0 ? ":" + port : "");
        String pathStr = (newPath.startsWith("/") ? newPath : "/" + newPath);

        String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            userInfo = userInfo + "@";
        } else {
            userInfo = "";
        }
        
        return uri.getScheme() + "://" + userInfo + uri.getHost() + portStr 
                + pathStr;
    }
    
    public static String setUserInfo(URI uri, String user, String password) {
        int port = uri.getPort();
        String path = uri.getPath();
        
        String portStr = (port > 0 ? ":" + port : "");
        String pathStr = (path != null ? path : "");

        String userInfo = user + ":" + password + "@";
        
        return uri.getScheme() + "://" + userInfo + uri.getHost() + portStr 
                + pathStr;
    }
    
}
