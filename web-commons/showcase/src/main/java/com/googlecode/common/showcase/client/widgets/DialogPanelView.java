
package com.googlecode.common.showcase.client.widgets;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.client.ui.panel.BaseOkCancelDialog;
import com.googlecode.common.client.ui.panel.ErrorPanel;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.ui.panel.Modal;
import com.googlecode.common.showcase.client.AbstractPanelView;


/**
 * {@link BaseOkCancelDialog} show-case.
 */
public final class DialogPanelView extends AbstractPanelView {

    private static final Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, DialogPanelView> {
    }
    
    private static final List<String>   CMD_LIST = Arrays.asList(
            ButtonType.OK.getCommand(), ButtonType.CANCEL.getCommand());
    

    public DialogPanelView() {
        initWidget(binder.createAndBindUi(this));
    }
    
    private Modal createModal() {
        Modal modal = new Modal("Modal header", true);
        modal.setBody(new HTML("<p>One fine body...</p>"));
        
        ButtonsPanel bp = modal.getButtonsPanel();
        bp.setActionProvider(new ActionProvider() {
            @Override
            public Collection<String> getActionCommands() {
                return CMD_LIST;
            }
            
            @Override
            public void actionPerformed(String actionCommand) {
            }
        });
        bp.addButton(ButtonType.OK);
        bp.addButton(ButtonType.CANCEL);
        return modal;
    }

    @UiHandler("demoButton")
    void onDemo(ClickEvent event) {
        final Modal modal = createModal();
        modal.getButtonsPanel().setActionProvider(new ActionProvider() {
            @Override
            public Collection<String> getActionCommands() {
                return CMD_LIST;
            }
            
            @Override
            public void actionPerformed(String actionCommand) {
                modal.hide();
            }
        });
        modal.show();
    }

    @UiHandler("msgButton")
    void onMessage(ClickEvent event) {
        MessageBox.showMessage("Hello World!");
    }

    @UiHandler("inputButton")
    void onInput(ClickEvent event) {
        final MessageBox box = new MessageBox();
        box.showInput("Please, enter a value", "", new Command() {
            @Override
            public void execute() {
                box.hide();
                MessageBox.showMessage("You entered: " + box.getInputValue());
            }
        });
    }

    @UiHandler("confirmButton")
    void onConfirm(ClickEvent event) {
        final MessageBox box = new MessageBox();
        box.showConfirm("Do You like Java?", new Command() {
            @Override
            public void execute() {
                box.hide();
                MessageBox.showMessage("You selected: " 
                        + box.getSelectedOption());
            }
        });
    }

    @UiHandler("errorButton")
    void onError(ClickEvent event) {
        ErrorPanel.showError("Test error message", new Exception());
    }

}
