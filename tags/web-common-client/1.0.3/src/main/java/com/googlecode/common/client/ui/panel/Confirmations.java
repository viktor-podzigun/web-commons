
package com.googlecode.common.client.ui.panel;

import com.google.gwt.user.client.Command;
import com.googlecode.common.client.ui.panel.MessageBox.CmdOption;


/**
 * This class is common for basic modal dialogs in project.
 */
public final class Confirmations {

    private static final String DELETE_ITEM = "Delete selected item?";
    
    
    private Confirmations() {
    }
    
    public static void deleteItem(final Command command) {
        final MessageBox box = new MessageBox();
        box.showConfirm(DELETE_ITEM, new Command() {
            @Override
            public void execute() {
                box.hide();
                if (box.getSelectedOption() == CmdOption.YES) {
                    command.execute();
                }
            }
        });
    }
    
}
