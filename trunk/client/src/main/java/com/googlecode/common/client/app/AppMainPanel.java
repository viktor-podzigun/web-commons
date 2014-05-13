
package com.googlecode.common.client.app;

import java.io.IOException;
import java.util.List;
import javax.inject.Singleton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.login.LoginService;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.MenuItem;
import com.googlecode.common.client.ui.PopupMenu;
import com.googlecode.common.protocol.login.AppMenuDTO;
import com.googlecode.common.protocol.login.LoginRedirectResponse;
import com.googlecode.common.protocol.login.LoginRespDTO;


/**
 * Application browse panel widget.
 */
@Singleton
public class AppMainPanel extends Composite {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, AppMainPanel> {
    }

    @UiField Anchor         app;
    @UiField FlowPanel      mainMenu;
    
    @UiField Element        userName;
    @UiField PopupMenu      userMenu;
    
    @UiField SimplePanel    container;
    @UiField Element        copyright;
    @UiField Element        version;
    
    private final AppBrowsePanel    browsePanel;
    private Command                 logoutCommand;

    
    public AppMainPanel() {
        initWidget(binder.createAndBindUi(this));
        
        browsePanel = new AppBrowsePanel();
        container.add(browsePanel);
        
        version.setInnerText(" (version: " + getVersion0() 
                + ", build: " + getBuild0() + ")");
    }
    
    public void setCopyright(String copyrightHtml) {
        copyright.setInnerHTML(copyrightHtml);
    }
    
    public AppBrowsePanel getBrowsePanel() {
        return browsePanel;
    }
    
    public void showPanel(Widget panel) {
        container.clear();
        container.add(panel);
    }
    
    public void setLogoutCommand(Command logoutCommand) {
        this.logoutCommand = logoutCommand;
    }

    public void setLoginRespDTO(LoginRespDTO dto) {
        userName.setInnerText(dto.getLogin());
        
        AppMenuDTO menuDto = dto.getAppMenu();
        if (menuDto != null) {
            createAppMenu(menuDto);
        }
        
        createUserMenu(dto);
    }
    
    private void createAppMenu(AppMenuDTO menuDto) {
        app.setText(menuDto.getTitle());
        String appUrl = menuDto.getUrl();
        if (appUrl != null) {
            app.setHref(appUrl);
        }
        
        List<AppMenuDTO> menuList = menuDto.safeGetSubMenu();
        if (!menuList.isEmpty()) {
            PopupMenu menu;
            Widget w = mainMenu.getWidget(0);
            if (w instanceof PopupMenu) {
                menu = (PopupMenu)w;
                menu.clear();
                createAppSubMenu(menu, true, menuList);
            } else {
                menu = createAppSubMenu(new PopupMenu(), true, menuList);
                menu.setStyleName("nav");
                mainMenu.insert(menu, 0);
            }
        }
    }
    
    private PopupMenu createAppSubMenu(PopupMenu menu, boolean isRootMenu, 
            List<AppMenuDTO> menuList) {
        
        for (AppMenuDTO app : menuList) {
            MenuItem item;
            List<AppMenuDTO> subList = app.safeGetSubMenu();
            if (subList.isEmpty()) {
                item = new MenuItem(app.getTitle());
                String url = app.getUrl();
                if (url != null) {
                    item.setTargetUrl(url);
                }
            } else {
                item = new MenuItem(app.getTitle(), createAppSubMenu(
                        new PopupMenu(), false, subList), isRootMenu);
            }
            
            menu.addMenuItem(item);
        }
        
        return menu;
    }
    
    private void createUserMenu(LoginRespDTO dto) {
        userMenu.clear();
        
        final String settingsUrl = dto.getSettingsUrl();
        if (settingsUrl != null) {
            userMenu.addMenuItem(new MenuItem("Settings", new Command() {
                @Override
                public void execute() {
                    Window.Location.assign(settingsUrl + "?continue=" 
                            + URL.encodeQueryString(Window.Location.getHref()));
                }
            }));
        }
        
        userMenu.addMenuItem(new MenuItem("Logout", new Command() {
            @Override
            public void execute() {
                TaskManager.INSTANCE.execute(new LogoutTask());
            }
        }));
    }

    private void doLogout(LoginRedirectResponse resp) {
        if (!AbstractClientApp.tryLoginRedirect(resp)) {
            if (logoutCommand != null) {
                logoutCommand.execute();
            } else {
                GWT.log("No logoutCommand was previously set");
            }
        }
    }

    private static native String getVersion0() /*-{
        var info = $wnd.appInfo;
        return (info ? info.version : "undefined");
    }-*/;
    
    private static native String getBuild0() /*-{
        var info = $wnd.appInfo;
        return (info ? info.build : "undefined");
    }-*/;


    private class LogoutTask extends RequestTask<LoginRedirectResponse> {

        public LogoutTask() {
            super("Logout user...");
        }

        @Override
        protected void runTask() throws IOException {
            LoginService service = GWT.create(LoginService.class);
            RequestService.prepare(service).logout(this);
        }

        @Override
        protected void processSuccessResponse(LoginRedirectResponse resp) {
            doLogout(resp);
        }
    }

}
