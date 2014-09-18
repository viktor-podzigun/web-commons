
package com.googlecode.common.client.ui.panel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.common.client.ui.AbstractButton;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.client.ui.CommonImages;
import com.googlecode.common.client.ui.TextField;


/**
 * Contains utility methods for showing information, question, warning, input
 * and other messages.
 */
public final class MessageBox {

    private Command     optionCommand;
    private TextField   textInput;
    private CmdOption   selectedOption = CmdOption.CLOSED;
    private CmdOption   defaultOption;
    
    private Modal       modal;
    
    
    public MessageBox() {
    }
    
    private void setOptionCommand(Command optionCommand) {
        this.optionCommand = optionCommand;
    }
    
    private void show(String message, String title, ImageResource icon,
            MsgOption optionType, String initialValue, boolean hasInput) {
        
        modal = new Modal(title, optionType != MsgOption.YES_NO);
        modal.setBody(createMainPanel(icon, message, optionType,
                initialValue, hasInput));
        
        createOptionsPanel(optionType);
        modal.show();
        
        // setFocusTextInput
        if (textInput != null) {
            textInput.setSelectionRange(0, textInput.getText().length());
            
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    textInput.setFocus(true);
                }
            });
        }
    }

    static String makeHtmlText(String text) {
        return text.replace("\n", "<br/>")
                .replace("\t", "&nbsp&nbsp&nbsp&nbsp");
    }
    
    private HTMLPanel createMainPanel(ImageResource icon, String text, 
            MsgOption optionType, String initialValue, boolean hasInput) {
        
        String iconId  = (icon != null ? DOM.createUniqueId() : null);
        String inputId = (hasInput ? DOM.createUniqueId() : null);
        
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"row-fluid\">");
        if (icon != null) {
            html.append("<input type=\"text\" id=\"").append(iconId)
                    .append("\"/>");
        }
        
        html.append("<p>").append(makeHtmlText(text)).append("</p>");
        if (hasInput) {
            html.append("<div class=\"control-group\">");
            html.append("<input type=\"text\" id=\"").append(inputId)
                    .append("\"/>");
            html.append("</div>");
        }
        
        html.append("</div>");
        
        HTMLPanel panel = new HTMLPanel(html.toString());
        if (icon != null) {
            panel.addAndReplaceElement(new Image(icon), iconId);
        }
        
        if (hasInput) {
            textInput = new TextField();
            textInput.addStyleName("span12");
            textInput.addKeyDownHandler(new KeyDownHandler() {
                @Override
                public void onKeyDown(KeyDownEvent event) {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        onSelectOption(defaultOption);
                    }
                }
            });
            if (initialValue != null) {
                textInput.setText(initialValue);
            }
            
            panel.addAndReplaceElement(textInput, inputId);
        } else {
            textInput = null;
        }
        
        return panel;
    }
    
    private void createOptionsPanel(MsgOption optionType) {
        ButtonsPanel bp = modal.getButtonsPanel();
        final List<String> commands = new ArrayList<String>();
        
        switch (optionType) {
        case DEFAULT:
            addOptionButton(commands, bp, "OK", CmdOption.OK, true);
            break;
        
        case YES_NO:
            addOptionButton(commands, bp, "Yes", CmdOption.YES, true);
            addOptionButton(commands, bp, "No", CmdOption.NO, false);
            break;
        
        case YES_NO_CANCEL:
            addOptionButton(commands, bp, "Yes", CmdOption.YES, true);
            addOptionButton(commands, bp, "No", CmdOption.NO, false);
            addOptionButton(commands, bp, "Cancel", CmdOption.CANCEL, false);
            break;
        
        case OK_CANCEL:
            addOptionButton(commands, bp, "OK", CmdOption.OK, true);
            addOptionButton(commands, bp, "Cancel", CmdOption.CANCEL, false);
            break;

        default:
            throw new RuntimeException("Unknown optionType: " + optionType);
        }
        
        bp.setActionProvider(new ActionProvider() {
            @Override
            public Collection<String> getActionCommands() {
                return commands;
            }
            
            @Override
            public void actionPerformed(String actionCommand) {
                CmdOption option = CmdOption.valueOf(actionCommand);
                if (option == CmdOption.CANCEL) {
                    modal.hide();
                    return;
                }
                
                onSelectOption(option);
            }
        });
    }
    
    private void onSelectOption(CmdOption option) {
        selectedOption = option;
        if (optionCommand != null) {
            optionCommand.execute();
            
            // Box should be hidden manually, from the command handler
            //hide();
        } else {
            modal.hide();
        }
    }
    
    private void addOptionButton(List<String> commands, ButtonsPanel bp, 
            String text, CmdOption option, boolean isDefault) {
        
        String id = option.name();
        commands.add(id);
        
        final AbstractButton btn = bp.addButton(new ButtonType(text, id, 
                option.getImage()));
        
        if (defaultOption == null && isDefault || option == defaultOption) {
            defaultOption = option;
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    btn.setFocus(true);
                }
            });
        }
    }
    
    public void setDefaultOption(CmdOption defaultOption) {
        this.defaultOption = defaultOption;
    }
    
    /**
     * Closes this message box.
     */
    public void hide() {
        modal.hide();
    }

    /**
     * Shows information message with specified text and OK button.
     * 
     * @param message text to display in MessageBox
     */
    public static void showMessage(String message) {
        new MessageBox().show(message, null, 
                MsgType.INFORMATION.getImage(), MsgOption.DEFAULT, 
                null, false);
    }

    /**
     * Shows information message with specified text, specified title and 
     * OK button.
     * 
     * @param message   text to display in MessageBox
     * @param title     title of MessageBox
     * @param messageType type of message
     */
    public static void showMessage(String message, String title,
            MsgType messageType) {
        
        new MessageBox().show(message, title, messageType.getImage(),
                MsgOption.DEFAULT, null, false);
    }

    /**
     * Shows information message with specified text, specified title and 
     * OK button.
     * 
     * @param message   text to display in MessageBox
     * @param title     title of MessageBox
     * @param icon      the icon to display in the dialog or <code>null</code> 
     *                  to use no icon
     */
    public static void showMessage(String message, String title,
            ImageResource icon) {
        
        new MessageBox().show(message, title, icon, MsgOption.DEFAULT, 
                null, false);
    }
    
    /**
     * Shows information message with specified text, specified title and 
     * OK button.
     * 
     * @param message       text to display in MessageBox
     * @param title         title of MessageBox
     * @param messageType   type of message
     * @param okCommand     command to execute when OK button is clicked, 
     *                      <code>null</code> can be used when no action 
     *                      is needed
     */
    public void showMessage(String message, String title, MsgType messageType, 
            Command okCommand) {
        
        setOptionCommand(okCommand);
        show(message, title, messageType.getImage(),
                MsgOption.DEFAULT, null, false);
    }

    /**
     * Shows confirmation message with specified text and buttons YES, NO,
     * CANCEL.
     * 
     * @param message       text to display in MessageBox
     * @param optionCommand command that will be executed when button is pressed. 
     *                      Nothing happens if null value of parameter is given.
     */
    public void showConfirm(String message, Command optionCommand) {
        setOptionCommand(optionCommand);
        show(message, null, MsgType.QUESTION.getImage(), 
                MsgOption.YES_NO, null, false);
    }

    /**
     * Shows confirmation message with specified text, specified title and
     * buttons.
     * 
     * @param message       text to display in MessageBox
     * @param title         title of MessageBox
     * @param optionType    options of buttons to be displayed such as YES_NO 
     *                      or OK_CANCEL and other
     * @param optionCommand command that will be executed when button will be 
     *                      pressed. Nothing happens if null value of parameter 
     *                      if given.
     */
    public void showConfirm(String message, String title, MsgOption optionType,
            Command optionCommand) {
        
        setOptionCommand(optionCommand);
        show(message, title, MsgType.QUESTION.getImage(), optionType, 
                null, false);
    }

    /**
     * Shows confirmation message with specified text, specified title,
     * specified icon and buttons.
     * 
     * @param message       text to display in MessageBox
     * @param title         title of MessageBox
     * @param icon          the icon to show in dialog. No icon if null value 
     *                      of parameter if given
     * @param optionType    options of buttons to be displayed such as YES_NO 
     *                      or OK_CANCEL and other
     * @param optionCommand command that will be executed when button will be 
     *                      pressed. Nothing happens if null value of parameter 
     *                      if given.
     */
    public void showConfirm(String message, String title, ImageResource icon,
            MsgOption optionType, Command optionCommand) {
        
        setOptionCommand(optionCommand);
        show(message, title, icon, optionType, null, false);
    }

    /**
     * Shows input dialog with specified text and buttons OK and CANCEL.
     * 
     * @param message       text to display in MessageBox
     * @param initialValue  initial text value in the input. Can be null value
     * @param optionCommand command that will be executed when button will be 
     *                      pressed. Nothing happens if null value of parameter 
     *                      if given.
     */
    public void showInput(String message, String initialValue,
            Command optionCommand) {
        
        setOptionCommand(optionCommand);
        show(message, null, null, MsgOption.OK_CANCEL, initialValue, true);
    }

    /**
     * Shows input dialog with specified text, specified title and buttons.
     * 
     * @param message       text to display in MessageBox
     * @param title         title of MessageBox
     * @param initialValue  initial text value in the input. Can be null value
     * @param optionType    options of buttons to be displayed such as YES_NO 
     *                      or OK_CANCEL and other
     * @param optionCommand command that will be executed when button will be 
     *                      pressed. Nothing happens if null value of parameter 
     *                      if given.
     */
    public void showInput(String message, String title, String initialValue,
            MsgOption optionType, Command optionCommand) {
        
        setOptionCommand(optionCommand);
        show(message, title, null, optionType, initialValue, true);
    }

    /**
     * Shows input dialog with specified text, specified title,
     * specified icon and buttons.
     * 
     * @param message       text to display in MessageBox
     * @param title         title of MessageBox
     * @param initialValue  initial text value in the input. Can be null value
     * @param icon          the icon to show in dialog. No icon if null value 
     *                      of parameter if given
     * @param optionType    options of buttons to be displayed such as YES_NO 
     *                      or OK_CANCEL and other
     * @param optionCommand command that will be executed when button will be 
     *                      pressed. Nothing happens if null value of parameter 
     *                      if given.
     */
    public void showInput(String message, String title, String initialValue,
            MsgOption optionType, ImageResource icon, Command optionCommand) {
        
        setOptionCommand(optionCommand);
        show(message, title, icon, optionType, initialValue, true);
    }

    /**
     * Returns selected option: YES, OK, CANCEL or other.
     * @return selected option: YES, OK, CANCEL or other
     */
    public CmdOption getSelectedOption() {
        return selectedOption;
    }
    
    /**
     * Returns text value that was entered in this input field.
     * @return text value that was entered in this input field
     */
    public String getInputValue() {
        return (textInput != null ? textInput.getText() : null);
    }
    

    /**
     * Message types, like: error, information, warning, question.
     */
    public static enum MsgType {
        
        ERROR(CommonImages.INSTANCE.dialogError()),
        INFORMATION(CommonImages.INSTANCE.dialogInfo()),
        WARNING(CommonImages.INSTANCE.dialogWarning()),
        QUESTION(CommonImages.INSTANCE.dialogQuestion()),
        ;
        
        private final ImageResource image;
        
        
        private MsgType(ImageResource image) {
            this.image = image;
        }
    
        public ImageResource getImage() {
            return image;
        }
    }

    
    /**
     * Options types, like: OK, OK_CANCEL, YES_NO, YES_NO_CANCEL.
     */
    public static enum MsgOption {
        DEFAULT,
        YES_NO,
        YES_NO_CANCEL,
        OK_CANCEL,
    }

    
    /**
     * User command options like YES, NO, CANCEL, OK, CLOSED.
     */
    public static enum CmdOption {
        
        YES,
        NO,
        CANCEL(ButtonImages.INSTANCE.cancel()),
        OK(ButtonImages.INSTANCE.ok()),
        CLOSED,
        ;
        
        private final ImageResource image;
        
        
        private CmdOption() {
            this(null);
        }
        
        private CmdOption(ImageResource image) {
            this.image = image;
        }
        
        public ImageResource getImage() {
            return image;
        }
    }
    
}
