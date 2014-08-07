
package com.googlecode.common.client.ui.panel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.googlecode.common.client.ui.AbstractButton;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;


/**
 * Base wizard dialog with three buttons: Back, Next and Cancel.
 */
public abstract class BaseWizardDialog extends BaseDialog {

    protected static final String   DEFAULT_BACK_TEXT = "<< Back";
    protected static final String   DEFAULT_NEXT_TEXT = "Next >>";
    
    private static final String     CMD_BACK = "back";
    
    private static final List<String> CMD_LIST = Arrays.asList(
            CMD_BACK, CMD_OK, CMD_CANCEL);
    
    private final AbstractButton    btnBack;
    private final AbstractButton    btnNext;
    
    
    public BaseWizardDialog(String title) {
        super(title);
        
        ButtonsPanel bp = getButtonsPanel();
        bp.setActionProvider(new ActionProvider() {
            @Override
            public Collection<String> getActionCommands() {
                return CMD_LIST;
            }
            
            @Override
            public void actionPerformed(String cmd) {
                if (cmd.equals(CMD_BACK)) {
                    onBackAction();
                
                } else if (cmd.equals(CMD_OK)) {
                    onNextAction();
                
                } else if (cmd.equals(CMD_CANCEL)) {
                    onCancelAction();
                }
            }
        });
        
        btnBack = bp.addButton(new ButtonType(DEFAULT_BACK_TEXT, CMD_BACK));
        btnBack.setVisible(false);
        
        btnNext = bp.addButton(new ButtonType(DEFAULT_NEXT_TEXT, CMD_OK));
        btnNext.addStyleName("btn-primary");
        
        bp.addButton(ButtonType.CANCEL);
    }
    
    protected abstract void onBackAction();
    
    protected abstract void onNextAction();
    
    protected void onCancelAction() {
        hide();
    }
    
    public void setBackVisible(boolean visible) {
        btnBack.setVisible(visible);
    }

    public void setNextText(String okText) {
        btnNext.setText(okText);
    }

}
