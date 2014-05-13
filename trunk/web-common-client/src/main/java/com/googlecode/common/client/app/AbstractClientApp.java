
package com.googlecode.common.client.app;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.common.client.app.login.LoginPanel;
import com.googlecode.common.client.app.login.LoginRedirectResponseCodec;
import com.googlecode.common.client.app.login.LoginTokenTask;
import com.googlecode.common.client.app.task.DefaultTaskManagerUi;
import com.googlecode.common.client.http.RequestErrorHandler;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.AbstractTask;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.panel.ErrorPanel;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;
import com.googlecode.common.protocol.login.LoginRedirectResponse;
import com.googlecode.common.protocol.login.LoginRespDTO;


/**
 * Contains common application entry point functionality.
 */
public abstract class AbstractClientApp implements EntryPoint {

    private AppMainPanel    mainPanel;
    
    
    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        GWT.log("onModuleLoad() started, baseURL: " 
                + RequestService.INSTANCE.getURL());
        
        TaskManager.INSTANCE.setUi(new DefaultTaskManagerUi());
        
        // register our global requests error handler
        RequestService.INSTANCE.setErrorHandler(new RequestErrorHandler() {
            @Override
            public boolean handleErrorStatus(AbstractTask task, Response resp) {
                if (resp.getStatusCode() == Response.SC_UNAUTHORIZED) {
                    relogin(resp);
                    return true;
                }
                
                return false;
            }
        });
        
        RootPanel.get("loading").setVisible(false);
        
        onLoginByToken();
        
        GWT.log("onModuleLoad() finished");
    }
    
    protected void onLoginByToken() {
        final LoginTokenTask loginTask = new LoginTokenTask();
        loginTask.setLoginCommand(new Command() {
            @Override
            public void execute() {
                LoginRespDTO resp = loginTask.getLoginRespDTO();
                if (resp != null) {
                    onLogin(resp);
                } else {
                    showLoginPanel();
                }
            }
        });
        
        TaskManager.INSTANCE.execute(loginTask);
    }
    
    public static boolean tryLoginRedirect(LoginRedirectResponse resp) {
        String loginRedirectUrl = resp.getLoginRedirectUrl();
        if (loginRedirectUrl != null) {
            // resolve relative path
            if (loginRedirectUrl.startsWith("/")) {
                loginRedirectUrl = RequestService.INSTANCE.getURL() 
                        + loginRedirectUrl;
            }
            
            Window.Location.replace(loginRedirectUrl + "?continue=" 
                    + URL.encodeQueryString(Window.Location.getHref()));
            return true;
        }
        
        return false;
    }
    
    private void relogin(Response response) {
        LoginRedirectResponse resp = LoginRedirectResponseCodec.INSTANCE.decode(
                JSONParser.parseLenient(response.getText()));
        
        if (!tryLoginRedirect(resp)) {
            showLoginPanel();
        }
    }
    
    private void showLoginPanel() {
        final LoginPanel loginPanel = new LoginPanel();
        loginPanel.setLoginCommand(new Command() {
            @Override
            public void execute() {
                RootPanel.get().remove(loginPanel);
                onLogin(loginPanel.getLoginRespDTO());
            }
        });
        
        if (mainPanel != null) {
            // if we show login panel after do logout
            loginPanel.setUserName(RequestService.INSTANCE.getUserLogin(), 
                    true);
            
            RootPanel.get().remove(mainPanel);
            
            // clean up tree
            BrowseTreeNode root = mainPanel.getBrowsePanel().getTreePanel()
                    .getRoot();
            // remove all nodes from root, in case we do re-login
            root.removeAll(true);
            
            mainPanel = null;
        }
        
        RootPanel.get().add(loginPanel);
    }
    
    protected void onLogin(LoginRespDTO loginDto) {
        mainPanel = createUI(loginDto);
    
        mainPanel.setLoginRespDTO(loginDto);
        mainPanel.setLogoutCommand(new Command() {
            @Override
            public void execute() {
                onLogout();
            }
        });
        
        RootPanel.get().add(mainPanel);
    
        // register uncaught exceptions handler
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable x) {
                ErrorPanel.showError(null, x);
            }
        });
    }
    
    private void onLogout() {
        showLoginPanel();
    }
    
    protected abstract AppMainPanel createUI(LoginRespDTO loginDto);
    
}
