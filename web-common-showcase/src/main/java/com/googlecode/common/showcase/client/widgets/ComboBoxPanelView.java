
package com.googlecode.common.showcase.client.widgets;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.task.AbstractTask;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.ComboBox;
import com.googlecode.common.client.ui.ImageLabel;
import com.googlecode.common.client.ui.LoadableComboBox;
import com.googlecode.common.showcase.client.AbstractPanelView;


/**
 * {@link ComboBox} show-case.
 */
public final class ComboBoxPanelView extends AbstractPanelView {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, ComboBoxPanelView> {
    }
    
    @UiField ComboBox<String>           comboBox;
    @UiField ImageLabel                 comboBoxLabel;
    
    @UiField LoadableComboBox<String>   loadableComboBox;
    @UiField ImageLabel                 loadableComboBoxLabel;


    public ComboBoxPanelView() {
        initWidget(binder.createAndBindUi(this));
        
        comboBox.setInitValue(null);
        comboBox.addAll(createItems());
        comboBox.setSelectCommand(new Command() {
            @Override
            public void execute() {
                comboBoxLabel.setText(comboBox.getSelected());
            }
        });
        
        loadableComboBox.setSelectCommand(new Command() {
            @Override
            public void execute() {
                loadableComboBoxLabel.setText(loadableComboBox.getSelected());
            }
        });
        loadableComboBox.setLoadCommand(new Command() {
            @Override
            public void execute() {
                TaskManager.INSTANCE.execute(new LoadTask());
            }
        });
    }
    
    private List<String> createItems() {
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 100; i++) {
            list.add("Item " + i);
        }
        
//        list.add("Item 1");
//        list.add("Item 2");
//        list.add("Item 3");
//        list.add("Item 4");
//        list.add("Item 5");
        return list;
    }

    @UiHandler("none")
    void onComboBoxDefaultStyle(ClickEvent event) {
        loadableComboBox.setStyleName("");
    }
    
    @UiHandler("inputXlarge")
    void onComboBoxXlargeStyle(ClickEvent event) {
        loadableComboBox.setStyleName("input-xlarge");
    }
    
    @UiHandler("inputMedium")
    void onComboBoxMediumStyle(ClickEvent event) {
        loadableComboBox.setStyleName("input-medium");
    }
    
    @UiHandler("inputMini")
    void onComboBoxMiniStyle(ClickEvent event) {
        loadableComboBox.setStyleName("input-mini");
    }
    
    @UiHandler("comboBoxEnable")
    void onComboBoxEnable(ClickEvent event) {
        comboBox.setEnabled(!comboBox.isEnabled());
    }

    @UiHandler("loadableComboBoxEnable")
    void onLoadableComboBoxEnable(ClickEvent event) {
        loadableComboBox.setEnabled(!loadableComboBox.isEnabled());
    }

    @UiHandler("loadableComboBoxEditable")
    void onLoadableComboBoxEditable(ClickEvent event) {
        loadableComboBox.setReadOnly(!loadableComboBox.isReadOnly());
    }


    private class LoadTask extends AbstractTask {
        
        public LoadTask() {
            super("Fetching data...");
        }
        
        @Override
        protected void runTask() throws Exception {
            // This timer is here to illustrate the asynchronous nature of
            // this data provider. In practice, you would use an 
            // asynchronous RPC call to request data in the specified range
            new Timer() {
                @Override
                public void run() {
                    loadableComboBox.setLoadedDataAndShow(createItems());
                    
                    onFinish();
                }
            }.schedule(1000);
        }
    }
    
}
