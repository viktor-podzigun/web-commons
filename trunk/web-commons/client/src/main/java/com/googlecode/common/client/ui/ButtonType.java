
package com.googlecode.common.client.ui;

import com.google.gwt.resources.client.ImageResource;


/**
 * Button definition.
 * 
 * <p>Contains button's command, text, and images.
 */
public final class ButtonType {
    
    // Common buttons definition
    
    public static final ButtonType REFRESH = new ButtonType("Refresh", "refresh", 
            ButtonImages.INSTANCE.refresh(), 
            ButtonImages.INSTANCE.refreshDisabled());
    
    public static final ButtonType ADD = new ButtonType("Add", "add", 
            ButtonImages.INSTANCE.add(), 
            ButtonImages.INSTANCE.addDisabled());
    
    public static final ButtonType REMOVE = new ButtonType("Remove", "remove", 
            ButtonImages.INSTANCE.delete(), 
            ButtonImages.INSTANCE.deleteDisabled());
    
    public static final ButtonType EDIT = new ButtonType("Edit", "edit", 
            ButtonImages.INSTANCE.edit(), 
            ButtonImages.INSTANCE.editDisabled());
    
    public static final ButtonType SAVE = new ButtonType("Save", "save", 
            ButtonImages.INSTANCE.save(), 
            ButtonImages.INSTANCE.saveDisabled());
    
    public static final ButtonType FIND = new ButtonType("Find", "find", 
            ButtonImages.INSTANCE.find());
    
    public static final ButtonType OPEN = new ButtonType("Open", "open", 
            ButtonImages.INSTANCE.folder(), 
            ButtonImages.INSTANCE.folderDisabled());
    
    public static final ButtonType INFO = new ButtonType("Info", "info", 
            ButtonImages.INSTANCE.info(), 
            ButtonImages.INSTANCE.infoDisabled());
    
    public static final ButtonType PRINT = new ButtonType("Print", "print", 
            ButtonImages.INSTANCE.print(), 
            ButtonImages.INSTANCE.printDisabled());
    
    public static final ButtonType OK = new ButtonType("OK", "ok", 
            ButtonImages.INSTANCE.ok(), 
            ButtonImages.INSTANCE.okDisabled());
    
    public static final ButtonType CANCEL = new ButtonType("Cancel", "cancel", 
            ButtonImages.INSTANCE.cancel(), 
            ButtonImages.INSTANCE.cancelDisabled());


    private final String        command;
    private final String        text;
    private final ImageResource image;
    private final ImageResource disabledImage;
    
    
    public ButtonType(String text, String command) {
        this(text, command, null, null);
    }
    
    public ButtonType(String text, String command, ImageResource image) {
        this(text, command, image, null);
    }
    
    public ButtonType(String text, String command, ImageResource image, 
            ImageResource disabledImage) {

        this.command        = command;
        this.text           = text;
        this.image          = image;
        this.disabledImage  = disabledImage;
    }
    
    public String getCommand() {
        return command;
    }
    
    public String getText() {
        return text;
    }

    public ImageResource getImage() {
        return image;
    }
    
    public ImageResource getDisabledImage() {
        return disabledImage;
    }

    @Override
    public String toString() {
        return command;
    }

}
