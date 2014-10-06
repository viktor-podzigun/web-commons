
package com.googlecode.common.client.app.login;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import com.googlecode.common.protocol.login.LoginRedirectResponse;
import com.googlecode.common.protocol.login.LoginRequests;
import com.googlecode.common.protocol.login.LoginResponse;


/**
 * Login service interface declaration.
 */
public interface LoginService extends RestService {

    @GET
    @Path(LoginRequests.LOGIN)
    public void login(@QueryParam("rm") boolean rememberMe, 
            MethodCallback<LoginResponse> callback);
    
    @GET
    @Path(LoginRequests.LOGIN_TOKEN)
    public void loginToken(MethodCallback<LoginResponse> callback);
    
    @GET
    @Path(LoginRequests.LOGOUT)
    public void logout(MethodCallback<LoginRedirectResponse> callback);
    
}
