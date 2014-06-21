
package com.googlecode.common.http;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.googlecode.common.util.UriHelpers;


/**
 * Contains optional request parameters.
 */
public final class RequestParams {

    private String              userName;
    private String              userPass;
    
    private String              userToken;
    
    private String              contentType;
    private int                 socketTimeout;
    private String              acceptLanguage;
    
    private String              urlPrefix;
    private Map<String, String> headers;
    
    
    public RequestParams() {
    }
    
    public RequestParams(String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;
    }
    
    public RequestParams(URI serverUrl) {
        String userInfo = serverUrl.getUserInfo();
        if (userInfo != null) {
            // parse user info from the given URL
            String[] info   = UriHelpers.splitUserInfo(userInfo);
            this.userName   = info[0];
            this.userPass   = info[1];
            this.urlPrefix  = UriHelpers.hideUserInfo(serverUrl);
        } else {
            this.urlPrefix  = serverUrl.toString();
        }
    }
    
    public RequestParams setUserCredentials(String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;
        return this;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public String getUserPass() {
        return userPass;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public RequestParams setJsonContentType() {
        contentType = "application/json";
        return this;
    }
    
    public RequestParams setXmlContentType() {
        contentType = "application/xml";
        return this;
    }
    
    public RequestParams setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public int getSocketTimeout() {
        return socketTimeout;
    }
    
    public RequestParams setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }
    
    public String getAcceptLanguage() {
        return acceptLanguage;
    }
    
    public void setAcceptLanguage(Locale acceptLanguage) {
        String lang = acceptLanguage.getLanguage();
        String country = acceptLanguage.getCountry();
        if (!country.isEmpty()) {
            this.acceptLanguage = lang + "-" + country.toLowerCase();
            return;
        }
        
        this.acceptLanguage = lang;
    }
    
    public String getUrlPrefix() {
        return urlPrefix;
    }
    
    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
    
    public boolean hasUserToken() {
        return (userToken != null);
    }

    public RequestParams addHeader(String name, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        
        headers.put(name, value);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
    
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "{"
                + (userName != null ? "userName: " + userName : "")
                + (contentType != null ? ", contentType: " + contentType : "")
                + ", socketTimeout: " + socketTimeout
                + (acceptLanguage != null ? ", acceptLanguage: " + acceptLanguage : "")
                + (urlPrefix != null ? ", urlPrefix: " + urlPrefix : "")
                + (headers != null ? ", headers: " + headers : "")
                + "}";
    }
    
}
