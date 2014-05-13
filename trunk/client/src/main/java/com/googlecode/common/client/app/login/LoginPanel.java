
package com.googlecode.common.client.app.login;

import java.io.IOException;
import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.PasswordField;
import com.googlecode.common.client.ui.TextField;
import com.googlecode.common.client.ui.text.TextChangeEvent;
import com.googlecode.common.client.ui.text.TextChangeEventHandler;
import com.googlecode.common.client.util.Base64;
import com.googlecode.common.client.util.StringHelpers;
import com.googlecode.common.protocol.login.LoginRespDTO;
import com.googlecode.common.protocol.login.LoginResponse;


/**
 * Login panel.
 */
public class LoginPanel extends Composite {

    private static Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, LoginPanel> {
    }
    
    @UiField Image          image;
    @UiField TextField      textName;
    @UiField PasswordField  textPassword;
    @UiField Button         btnLogin;
    @UiField CheckBox       rememberMe;
    
    private Command         loginCommand;
    private LoginRespDTO    loginRespDto;

    
    public LoginPanel() {
        initWidget(binder.createAndBindUi(this));
        
        //Add handler for enable/disable login button
        textName.addTextChangeEventHandler(new TextChangeEventHandler() {
            @Override
            public void onTextChange(TextChangeEvent event) {
            	onInputChanged();
            }
        });
        
        //Add handler for enable/disable login button
        textPassword.addTextChangeEventHandler(new TextChangeEventHandler() {
            @Override
            public void onTextChange(TextChangeEvent event) {
            	onInputChanged();
            }
        });
    }
    
	private void onInputChanged() {
		btnLogin.setEnabled(!textName.getValue().isEmpty()
				&& !textPassword.getValue().isEmpty());
	}

    @Override
    protected void onLoad() {
        super.onLoad();
    
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                if (StringHelpers.isNullOrEmpty(textName.getText())) {
                    textName.setFocus(true);
                } else {
                    textPassword.setFocus(true);
                }
            }
        });
    }
    
    public void setImage(ImageResource resource) {
        image.setResource(resource);
    }
    
    public void setLoginCommand(Command loginCommand) {
        this.loginCommand = loginCommand;
    }
    
    public LoginRespDTO getLoginRespDTO() {
        return loginRespDto;
    }
    
    public String getUserName() {
        return textName.getValue();
    }
    
    public void setUserName(String name, boolean enabled) {
        textName.setValue(name);
        textName.setEnabled(enabled);
        
        // set focus to the password field
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                textPassword.setFocus(true);
            }
        });
    }
    
    @UiHandler("textName")
    void onNameKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER 
                && btnLogin.isEnabled()) {
            
            onLogin();
        }
    }

    @UiHandler("textPassword")
    void onPasswordKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER 
                && btnLogin.isEnabled()) {
            
            onLogin();
        }
    }

    @UiHandler("btnLogin")
    void onBtnLoginClick(ClickEvent event) {
        onLogin();
    }
    
    private void onLogin() {
        TaskManager.INSTANCE.execute(new LoginTask());
    }


    private class LoginTask extends RequestTask<LoginResponse> implements 
            Dispatcher {

        private final String    authHeader; // cached user info
        
        public LoginTask() {
            super("Login user...");
            
            try {
                String info = getUserName() + ":" + textPassword.getValue();
                StringBuilder auth = new StringBuilder("Basic ").append(
                        Base64.encodeToChar(info.getBytes("UTF-8"), false));
                
                this.authHeader = auth.toString();
            
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }

        @Override
        public Request send(Method method, RequestBuilder builder)
                throws RequestException {
            
            builder.setHeader("Authorization", authHeader);
            return builder.send();
        }
        
        @Override
        protected void runTask() throws IOException {
            LoginService service = GWT.create(LoginService.class);
            RestServiceProxy proxy = (RestServiceProxy)service;
            proxy.setResource(new Resource(RequestService.INSTANCE.getURL()));
            proxy.setDispatcher(this);
            
            boolean rm = (rememberMe.getValue() != null ? 
                    rememberMe.getValue().booleanValue() : false);
            service.login(rm, this);
        }

        @Override
        protected void processSuccessResponse(LoginResponse resp) {
            loginRespDto = resp.getData();
            if (loginCommand != null) {
                loginCommand.execute();
            } else {
                GWT.log("No loginCommand was previously set");
            }
        }
        
        @Override
        protected boolean handleErrorStatus(Response response) {
            return false;
        }
    }

}
