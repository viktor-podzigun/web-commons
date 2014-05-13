
package com.googlecode.common.web;

import java.net.URI;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.googlecode.common.io.FileHelpers;
import com.googlecode.common.service.AuthData;


/**
 * Contains useful methods for working with requests and responses.
 */
public final class ServletHelpers {

    private static final ThreadLocal<HttpServletRequest> THREAD_LOCAL_REQUEST = 
        new ThreadLocal<HttpServletRequest>() {
            @Override
            protected HttpServletRequest initialValue() {
                return null;
            }
        };
    
    private static volatile String  appDomain = ".app.domain";
    
    
    private ServletHelpers() {
    }
    
    /**
     * Sets current application domain to use with saved cookies.
     * 
     * <p>Domain name should begins with a dot (<code>.foo.bar</code>).
     * 
     * @param appDomain application domain to use
     */
    public static void setAppDomain(String appDomain) {
        if (!appDomain.startsWith(".")) {
            throw new IllegalArgumentException(
                    "Domain name should begins with a dot, appDomain: " 
                    + appDomain);
        }
        
        ServletHelpers.appDomain = appDomain;
    }

    /**
     * Returns current application domain.
     * @return current application domain, without leading dot
     */
    public static String getAppDomain() {
        return appDomain;
    }

    /**
     * Prepares response content type and headers for downloading 
     * the specified file.
     * 
     * @param req       HTTP request
     * @param resp      HTTP response
     * @param fileName  file name to download
     */
    public static void prepareFileDownload(HttpServletRequest req, 
            HttpServletResponse resp, String fileName) {

        // get the response type according to filename
        ServletContext sc = req.getSession().getServletContext();
        String mime = sc.getMimeType(fileName);
        
        resp.setContentType(mime != null && !mime.isEmpty() ? 
                mime : "application/" + FileHelpers.getFileExtension(fileName));
        
        // set content specific information
        resp.setHeader("Content-disposition", 
                "attachment;filename=" + fileName);
    }
    
    /**
     * Returns authorization info in the format: [login, password] 
     * from the given request object.
     * 
     * @param request   request object
     * @return          authorization info in the format: [login, password] or 
     *                  <code>null</code> if no authorization header 
     *                  was specified, or authorization scheme is not basic
     */
    public static String[] getBasicAuthInfo(HttpServletRequest request) {
        AuthData auth = AuthData.parse(request);
        if (auth == null || auth.isToken()) {
            return null;
        }

        return new String[]{auth.getLogin(), auth.getPassword()};
    }
    
    /**
     * Returns authorization token from the given request object.
     * 
     * @param request   request object
     * @return          authorization token or <code>null</code> if 
     *                  no authorization header was specified, 
     *                  or authorization scheme is not token
     */
    public static String getAuthToken(HttpServletRequest request) {
        AuthData auth = AuthData.parse(request);
        if (auth == null || !auth.isToken()) {
            return null;
        }
        
        return auth.getToken();
    }
    
    /**
     * Returns the preferred <code>Locale</code> that the client will accept
     * content in, based on the Accept-Language header. If the client request
     * doesn't provide an Accept-Language header, this method returns the
     * default locale for the server.
     * 
     * @return the preferred <code>Locale</code> for the client
     */
    public static Locale getRequestLocale() {
        HttpServletRequest req = getRequest();
        if (req != null) {
            return req.getLocale();
        }
        
        return Locale.getDefault();
    }
    
    /**
     * Returns current request's client IP address.
     * 
     * <p>This method tries to get IP from <i>X-Forwarded-For</i> HTTP header 
     * first, and if it does't exist returns the value of calling the 
     * <code>HttpServletRequest.getRemoteAddr()</code> method.
     * 
     * @return current request's client IP address
     */
    public static String getRequestAddr() {
        HttpServletRequest req = getRequest();
        if (req == null) {
            return null;
        }
        
        String forwarded = req.getHeader("X-Forwarded-For");
        if (forwarded == null) {
            return req.getRemoteAddr();
        }
        
        return forwarded.split(",", 2)[0];
    }
    
    /**
     * Reconstructs the URL the client used to make the request.
     * 
     * <p>The returned URL contains a protocol, server name, port number, 
     * and server path, but it does not include query string parameters.
     * 
     * <p>This method correctly handles <i>X-Forwarded-Proto</i>, 
     * <i>X-Forwarded-For</i> HTTP headers, if specified.
     * 
     * @param req   HTTP request
     * @return      URL from the given HTTP request
     */
    public static String getRequestUrl(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder(128);
        sb.append(req.getScheme()).append("://")
                .append(req.getServerName());
        
        URI url = URI.create(req.getRequestURL().toString());
        int port = url.getPort();
        if (port != -1) {
            sb.append(":").append(port);
        }
        
        String path = url.getPath();
        if (path != null) {
            sb.append(path);
        }
        
        return sb.toString();
    }
    
    /**
     * Returns current request.
     * @return current request
     */
    public static HttpServletRequest getRequest() {
        return THREAD_LOCAL_REQUEST.get();
    }
    
    /**
     * Sets current request.
     * 
     * @param req   current request
     */
    static void setRequest(HttpServletRequest req) {
        THREAD_LOCAL_REQUEST.set(req);
    }

    /**
     * Returns cookie value by the given cookie name.
     * 
     * @param cookies       cookies from request
     * @param cookieName    cookie name
     * @param defaultValue  default value to return in case the given cookie 
     *                      is not found
     * @return              cookie value or default value if cookie not exists
     */
    public static String getCookieValue(Cookie[] cookies, String cookieName,
            String defaultValue) {
        
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        
        return defaultValue;
    }

    /**
     * Adds the given token to the given response cookies.
     * 
     * @param resp      HTTP response
     * @param token     user token string
     * @return          added cookie
     */
    public static Cookie addTokenCookie(HttpServletResponse resp, 
            String token) {
        
        return addCookie(resp, AuthData.TOKEN_NAME, token, appDomain);
    }

    /**
     * Removes token from cookies.
     * 
     * @param resp      HTTP response
     * @return          removed cookie
     */
    public static Cookie removeTokenCookie(HttpServletResponse resp) {
        return removeCookie(resp, AuthData.TOKEN_NAME);
    }
    
    /**
     * Adds new long-lived cookie with the given info to the given response 
     * cookies.
     * 
     * @param resp      HTTP response
     * @param name      cookie name
     * @param value     cookie value
     * @param domain    domain for cookie, optional
     * @return          added cookie
     */
    public static Cookie addCookie(HttpServletResponse resp, 
            String name, String value, String domain) {
        
        Cookie cookie = new LongLivedCookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        
        if (domain != null) {
            HttpServletRequest req = getRequest();
            if (req != null && req.getServerName().endsWith(domain)) {
                cookie.setDomain(domain);
            }
        }
        
        resp.addCookie(cookie);
        return cookie;
    }
    
    /**
     * Removes cookie with the given name from cookies.
     * 
     * @param resp      HTTP response
     * @param name      cookie name
     * @return          removed cookie
     */
    public static Cookie removeCookie(HttpServletResponse resp, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete token cookie
        resp.addCookie(cookie);
        return cookie;
    }
    
}
