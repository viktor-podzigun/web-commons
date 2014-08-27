
package com.googlecode.common.showcase.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ExternalTextResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.app.AppMainPanel;
import com.googlecode.common.client.config.ui.ConfigEditPanel;
import com.googlecode.common.client.task.AbstractTextTask;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.ImageButton;
import com.googlecode.common.client.ui.TextAreaField;
import com.googlecode.common.client.ui.panel.LoadablePanel;


public final class ConfigPanelView extends LoadablePanel {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, ConfigPanelView> {
    }

    @UiField ImageButton        btnConfig;
    @UiField TextAreaField      textSchema;
    @UiField TextAreaField      textData;
    
    private final AppMainPanel  mainPanel;
    
    
    public ConfigPanelView(AppMainPanel mainPanel, String name) {
        this.mainPanel = mainPanel;
        
        initWidget(binder.createAndBindUi(this));
        
        btnConfig.setImage(ButtonImages.INSTANCE.edit());
    }
    
    @Override
    public void onDeactivated() {
        setNeedLoad(false);
    }
    
    @Override
    public void onLoadData() {
        TaskManager.INSTANCE.execute(new LoadSchemaTask(
                JsonResources.INSTANCE.configSchemaJson()));
        
        TaskManager.INSTANCE.execute(new LoadDataTask(
                JsonResources.INSTANCE.configDataJson()));
    }
    
    @UiHandler("btnConfig")
    void onBtnConfigClick(ClickEvent event) {
        ConfigEditPanel dlg = new ConfigEditPanel(mainPanel, 
                "Edit configuration", 
                JSONParser.parseStrict(textSchema.getText().trim()).isObject(), 
                JSONParser.parseStrict(textData.getText().trim()).isObject());
        dlg.show();
    }
    
    
    interface JsonResources extends ClientBundle {
        
        static final JsonResources INSTANCE = GWT.create(JsonResources.class);

        @Source("config.data.json")
        ExternalTextResource configDataJson();
    
        @Source("config.schema.json")
        ExternalTextResource configSchemaJson();
    }
    
    
    private class LoadSchemaTask extends AbstractTextTask {
        
        public LoadSchemaTask(ExternalTextResource res) {
            super("Loading config schema...", res);
        }
        
        @Override
        public void processResult(String text) {
            textSchema.setText(text);
        }
    }

    
    private class LoadDataTask extends AbstractTextTask {
        
        public LoadDataTask(ExternalTextResource res) {
            super("Loading config data...", res);
        }
        
        @Override
        public void processResult(String text) {
            textData.setText(text);
        }
    }

}
