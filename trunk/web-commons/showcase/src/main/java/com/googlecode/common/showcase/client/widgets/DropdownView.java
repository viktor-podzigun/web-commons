
package com.googlecode.common.showcase.client.widgets;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.client.ui.DropdownButton;
import com.googlecode.common.client.ui.MenuItem;
import com.googlecode.common.client.ui.PopupMenu;
import com.googlecode.common.client.ui.SplitButton;
import com.googlecode.common.showcase.client.AbstractPanelView;


/**
 * {@link DropdownButton} show-case.
 */
public final class DropdownView extends AbstractPanelView implements 
        ActionProvider {

    private static final List<String> CMD_LIST = Arrays.asList(
            ButtonType.ADD.getCommand(), 
            ButtonType.EDIT.getCommand());
    
    private static final Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, DropdownView> {
    }
    
    @UiField(provided=true)
    DropdownButton      dropdownButton;

    @UiField(provided=true)
    ButtonsPanel        dropdownPanel;

    @UiField(provided=true)
    SplitButton         splitButton;

    @UiField(provided=true)
    ButtonsPanel        splitPanel;


    public DropdownView() {
        Command cmd = new Command() {
            @Override
            public void execute() {
            }
        };
        
        dropdownButton = new DropdownButton("Action", createPopupMenu(cmd));
        dropdownButton.setImage(ButtonImages.INSTANCE.add());
        
        DropdownButton ddButton = new DropdownButton("Action", 
                createPopupMenu(cmd));
        ddButton.setCommand(ButtonType.ADD.getCommand());
        ddButton.setImage(ButtonImages.INSTANCE.add());
        
        dropdownPanel = new ButtonsPanel();
        dropdownPanel.addButton(ddButton);
        dropdownPanel.addButton(ButtonType.EDIT);
        dropdownPanel.setActionProvider(this);
        
        splitButton = new SplitButton("Action", createPopupMenu(cmd), 
                new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            }
        });
    
        SplitButton sButton = new SplitButton("Action", createPopupMenu(cmd));
        sButton.setCommand(ButtonType.ADD.getCommand());
        
        splitPanel = new ButtonsPanel();
        splitPanel.addButton(sButton);
        splitPanel.addButton(ButtonType.EDIT);
        splitPanel.setActionProvider(this);
        
        initWidget(binder.createAndBindUi(this));
    }
    
    private PopupMenu createPopupMenu(Command cmd) {
        PopupMenu subMenu = new PopupMenu();
        subMenu.addMenuItem(new MenuItem("Sub Action", cmd));
        MenuItem disabled = new MenuItem("Disabled", cmd);
        disabled.setEnabled(false);
        subMenu.addMenuItem(disabled);
        
        PopupMenu menu = new PopupMenu();
        menu.addMenuItem(new MenuItem(ButtonImages.INSTANCE.add(), 
                "Action 1", cmd));
        menu.addMenuItem(new MenuItem(null, "Action 2", cmd));
        menu.addSeparator();
        menu.addMenuItem(new MenuItem(null, "Sub Menu", subMenu));
        return menu;
    }

    @Override
    public Collection<String> getActionCommands() {
        return CMD_LIST;
    }

    @Override
    public void actionPerformed(String actionCommand) {
    }
    
}
