
package com.googlecode.common.protocol.login;

import com.googlecode.common.protocol.BaseResponse;


/**
 * Part of server responses, containing login redirect info.
 */
public final class LoginRedirectResponse extends BaseResponse {

    private String  loginRedirectUrl;
    
    
    public LoginRedirectResponse() {
    }
    
    public LoginRedirectResponse(String loginRedirectUrl) {
        this.loginRedirectUrl = loginRedirectUrl;
    }
    
    public String getLoginRedirectUrl() {
        return loginRedirectUrl;
    }
    
    public void setLoginRedirectUrl(String loginRedirectUrl) {
        this.loginRedirectUrl = loginRedirectUrl;
    }
    
}
