
package com.googlecode.common.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


/**
 * Defines common buttons images.
 */
public interface ButtonImages extends ClientBundle {

    public static final ButtonImages INSTANCE = GWT.create(ButtonImages.class);
    
    
    @Source("buttons/refresh.png")
    public ImageResource refresh();
    
    @Source("buttons/refresh_disabled.png")
    public ImageResource refreshDisabled();
    
    @Source("buttons/add.png")
    public ImageResource add();
    
    @Source("buttons/add_disabled.png")
    public ImageResource addDisabled();
    
    @Source("buttons/delete.png")
    public ImageResource delete();
    
    @Source("buttons/delete_disabled.png")
    public ImageResource deleteDisabled();
    
    @Source("buttons/pencil.png")
    public ImageResource edit();
    
    @Source("buttons/pencil_disabled.png")
    public ImageResource editDisabled();
    
    @Source("buttons/disk.png")
    public ImageResource save();
    
    @Source("buttons/disk_disabled.png")
    public ImageResource saveDisabled();
    
    @Source("buttons/find.png")
    public ImageResource find();
    
    @Source("buttons/folder.png")
    public ImageResource folder();
    
    @Source("buttons/folder_disabled.png")
    public ImageResource folderDisabled();
    
    @Source("buttons/info.png")
    public ImageResource info();
    
    @Source("buttons/info_disabled.png")
    public ImageResource infoDisabled();
    
    @Source("buttons/printer.png")
    public ImageResource print();
    
    @Source("buttons/printer_disabled.png")
    public ImageResource printDisabled();
    
    @Source("buttons/accept.png")
    public ImageResource ok();
    
    @Source("buttons/accept_disabled.png")
    public ImageResource okDisabled();
    
    @Source("buttons/cancel.png")
    public ImageResource cancel();

    @Source("buttons/cancel_disabled.png")
    public ImageResource cancelDisabled();

    @Source("buttons/db_save.png")
    public ImageResource dbSave();

    @Source("buttons/db_save_disabled.png")
    public ImageResource dbSaveDisabled();

}
