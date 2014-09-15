
package com.googlecode.common.service;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import com.googlecode.common.web.ServletHelpers;


/**
 * Authentication request data parser.
 */
public final class AuthData {

    public static final String      TOKEN_NAME  = "auth-token";
    
    private static final Pattern    SPACE_SPLIT = Pattern.compile(" ");
    private static final Pattern    COLON_SPLIT = Pattern.compile(":");
    
    private final String            login;
    private final String            password;
    private final String            token;
    

    private AuthData(String login, String password, String token) {
        this.login      = login;
        this.password   = password;
        this.token      = token;
    }
    
    public static AuthData parse(HttpServletRequest request) {
        return parse(request, TOKEN_NAME);
    }
    
    public static AuthData parse(HttpServletRequest request, String tokenName) {
        AuthData auth = parseHeader(request);
        if (auth != null) {
            return auth;
        }
        
        return parseCookie(request, tokenName);
    }
    
    private static AuthData parseCookie(HttpServletRequest request, 
            String tokenName) {
        
        String token = ServletHelpers.getCookieValue(request.getCookies(), 
                tokenName, null);
        if (token == null) {
            return null;
        }
        
        return new AuthData(null, null, token);
    }
    
    private static AuthData parseHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("authorization");
        if (authHeader == null) {
            return null;
        }

        String[] authData = SPACE_SPLIT.split(authHeader, 2);
        if (authData.length != 2) {
            return null;
        }
        
        String authScheme = authData[0].toLowerCase();
        if ("basic".equals(authScheme)) {
            String base64 = authData[1];
            String userInfo = new String(Base64.decodeBase64(base64));
            String[] userPass = COLON_SPLIT.split(userInfo, 2);
            if (userPass.length != 2) {
                return null;
            }
            
            return new AuthData(userPass[0], userPass[1], null);
        }

        if ("token".equals(authScheme)) {
            return new AuthData(null, null, authData[1]);
        }
        
        return null;
    }
    
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
    
    public String getToken() {
        return token;
    }
    
    public boolean isToken() {
        return (token != null);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{"
                + (login != null ? "login: " + login : "")
                + (token != null ? "token: " + token : "") 
                + "}";
    }

}
