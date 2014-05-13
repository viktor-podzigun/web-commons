
package com.googlecode.common.web;

import javax.servlet.http.Cookie;


/**
 * Cookie that automatically persist when the client quits the browser.
 * 
 * <p>Got from here:<br>
 * http://www.apl.jhu.edu/~hall/java/Servlet-Tutorial/Servlet-Tutorial-Cookies.html
 */
public final class LongLivedCookie extends Cookie {

    private static final long serialVersionUID = -6813379541610867089L;
    
    private static final int    SECONDS_PER_MOUNTH = 60 * 60 * 24 * 30;
    
    
    public LongLivedCookie(String name, String value) {
        super(name, value);
        
        setMaxAge(SECONDS_PER_MOUNTH);
    }

}
