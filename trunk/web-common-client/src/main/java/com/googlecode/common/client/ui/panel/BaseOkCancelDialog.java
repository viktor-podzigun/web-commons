
package com.googlecode.common.client.ui.panel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.googlecode.common.client.ui.AbstractButton;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;


/**
 * Base dialog with two buttons: Ok and Cancel.
 * <p>
 * Text and icon for Ok button can be customized. This allows to use 
 * this dialog for different operations like: Save, Select, etc.
 */
public class BaseOkCancelDialog extends BaseDialog {

    private static final List<String> CMD_LIST = Arrays.asList(
            CMD_OK, CMD_CANCEL);
    
    private final AbstractButton    btnOk;
    
    private Command                 okCommand;
    
    
    public BaseOkCancelDialog(String title) {
        this(title, ButtonType.OK.getText());
    }
        
    public BaseOkCancelDialog(String title, String okActionName) {
        super(title);
        
        ButtonsPanel bp = getButtonsPanel();
        bp.setActionProvider(new ActionProvider() {
            @Override
            public Collection<String> getActionCommands() {
                return CMD_LIST;
            }
            
            @Override
            public void actionPerformed(String cmd) {
                if (cmd.equals(CMD_OK)) {
                    if (okCommand != null) {
                        okCommand.execute();
                    }
                } else if (cmd.equals(CMD_CANCEL)) {
                    onCancelAction();
                }
            }
        });
        
        btnOk = bp.addButton(ButtonType.OK);
        btnOk.addStyleName("btn-primary");
        setOkText(okActionName);
        
        bp.addButton(ButtonType.CANCEL);
    }
    
    protected void onCancelAction() {
        hide();
    }
    
    public void setOkCommand(Command okCommand) {
        this.okCommand = okCommand;
    }
    
    public void setOkEnabled(boolean enabled) {
        btnOk.setEnabled(enabled);
    }

    public void setOkText(String okText) {
        btnOk.setText(okText);
    }

    public void setOkIcon(ImageResource icon, ImageResource disabledIcon) {
        btnOk.setImage(icon);
        btnOk.setDisabledImage(disabledIcon);
    }

}
