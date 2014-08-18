
package com.googlecode.common.showcase.client.widgets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.AppMainPanel;
import com.googlecode.common.client.app.task.RequestTask;
import com.googlecode.common.client.config.ui.ConfigEditPanel;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ImageButton;
import com.googlecode.common.client.ui.TextField;
import com.googlecode.common.protocol.DataResponse;


public final class ConfigPanelView extends Composite {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, ConfigPanelView> {
    }

    @UiField TextField      textName;
    @UiField TextField      textId;
    @UiField ImageButton    btnConfig;
    
    private final AppMainPanel mainPanel;
    
    
    public ConfigPanelView(AppMainPanel mainPanel, String name) {
        this.mainPanel = mainPanel;
        
        initWidget(binder.createAndBindUi(this));
    
        textName.setText(name);
        textName.setReadOnly(true);
        
        textId.setText("67071c0a-f862-4799-8893-29e08ac40940");
        textId.setReadOnly(true);
    }
    
    @UiHandler("btnConfig")
    void onBtnConfigClick(ClickEvent event) {
        TaskManager.INSTANCE.execute(new LoadConfigTask());
    }
    
    private void showConfigPanel(JSONValue resp) {
        ConfigEditPanel dlg = new ConfigEditPanel(mainPanel, 
                textName.getText() + " configuration", resp);
        dlg.show();
    }
    

    /**
     * Configuration service interface declaration.
     */
    interface ConfigService extends RestService {

        @GET
        @Path("/config")
        public void getTestData(MethodCallback<DataResponse<JSONValue>> callback);

    }
    
    
    private class LoadConfigTask extends RequestTask<DataResponse<JSONValue>> {
        
        public LoadConfigTask() {
            super("Loading data...");
        }
        
        @Override
        protected void runTask() throws Exception {
            ConfigService service = GWT.create(ConfigService.class);
            RequestService.prepare(service).getTestData(this);
        }
        
        @Override
        protected void processSuccessResponse(DataResponse<JSONValue> resp) {
            showConfigPanel(resp.getData());
        }
    }
    
}
