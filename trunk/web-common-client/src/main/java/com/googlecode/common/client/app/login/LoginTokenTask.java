
package com.googlecode.common.client.app.login;

import java.io.IOException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.protocol.login.LoginRespDTO;
import com.googlecode.common.protocol.login.LoginResponse;


/**
 * Login by token task.
 * 
 * <p>Token is taken from cookies.
 */
public final class LoginTokenTask extends RequestTask<LoginResponse> {

    private Command         loginCommand;
    private LoginRespDTO    loginRespDto;
    
    
    public LoginTokenTask() {
        super("Login user...");
    }
    
    public void setLoginCommand(Command loginCommand) {
        this.loginCommand = loginCommand;
    }

    public LoginRespDTO getLoginRespDTO() {
        return loginRespDto;
    }
    
    @Override
    protected void runTask() throws IOException {
        LoginService service = GWT.create(LoginService.class);
        RequestService.prepare(service).loginToken(this);
    }
    
    @Override
    protected void processFailureResponse(LoginResponse resp) {
        super.processFailureResponse(resp);
        executeLoginCommand();
    }

    @Override
    protected void processSuccessResponse(LoginResponse resp) {
        loginRespDto = resp.getData();
        executeLoginCommand();
    }
    
    private void executeLoginCommand() {
        if (loginCommand != null) {
            loginCommand.execute();
        } else {
            GWT.log("No loginCommand was previously set");
        }
    }
    
}
