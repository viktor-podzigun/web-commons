
package com.googlecode.common.showcase.client.widgets;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.AnchorButton;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.showcase.client.AbstractPanelView;


/**
 * {@link ButtonsPanel} show-case.
 */
public final class ButtonsPanelView extends AbstractPanelView implements 
        ActionProvider {

    private static final String         CMD_EXPORT = "export";
    
    private static final List<String>   CMD_LIST = Arrays.asList(
            ButtonType.ADD.getCommand(), 
            ButtonType.EDIT.getCommand(), 
            CMD_EXPORT);
    
    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, ButtonsPanelView> {
    }
    
    @UiField(provided=true) ButtonsPanel    bp1;
    @UiField(provided=true) ButtonsPanel    bp2;

    
    public ButtonsPanelView() {
        bp1 = new ButtonsPanel(ButtonType.ADD, ButtonType.REMOVE, 
                ButtonType.EDIT);
        
        AnchorButton exportBtn = new AnchorButton(
                ButtonImages.INSTANCE.save(), "Export");
        exportBtn.setDisabledImage(ButtonImages.INSTANCE.saveDisabled());
        exportBtn.setCommand(CMD_EXPORT);
        bp1.addButton(exportBtn);
        bp1.setActionProvider(this);
        bp1.setShowText(false);
        bp1.setGroup(true);
        
        bp2 = new ButtonsPanel(ButtonType.ADD, ButtonType.REMOVE, 
                ButtonType.EDIT);
        
        AnchorButton exportBtn2 = new AnchorButton(
                ButtonImages.INSTANCE.save(), "Export");
        exportBtn2.setDisabledImage(ButtonImages.INSTANCE.saveDisabled());
        exportBtn2.setCommand(CMD_EXPORT);
        exportBtn2.setHref("#");
        exportBtn2.setPrimary(true);
        bp2.addButton(exportBtn2);
        bp2.setActionProvider(this);
        
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public Collection<String> getActionCommands() {
        return CMD_LIST;
    }

    @Override
    public void actionPerformed(String actionCommand) {
    }

}
