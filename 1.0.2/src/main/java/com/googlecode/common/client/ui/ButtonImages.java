
package com.googlecode.common.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


/**
 * Defines common buttons images.
 */
public interface ButtonImages extends ClientBundle {

    public static final ButtonImages INSTANCE = GWT.create(ButtonImages.class);
    
    
    @Source("com/googlecode/common/client/ui/buttons/refresh.png")
    public ImageResource refresh();
    
    @Source("com/googlecode/common/client/ui/buttons/refresh_disabled.png")
    public ImageResource refreshDisabled();
    
    @Source("com/googlecode/common/client/ui/buttons/add.png")
    public ImageResource add();
    
    @Source("com/googlecode/common/client/ui/buttons/add_disabled.png")
    public ImageResource addDisabled();
    
    @Source("com/googlecode/common/client/ui/buttons/delete.png")
    public ImageResource delete();
    
    @Source("com/googlecode/common/client/ui/buttons/delete_disabled.png")
    public ImageResource deleteDisabled();
    
    @Source("com/googlecode/common/client/ui/buttons/pencil.png")
    public ImageResource edit();
    
    @Source("com/googlecode/common/client/ui/buttons/pencil_disabled.png")
    public ImageResource editDisabled();
    
    @Source("com/googlecode/common/client/ui/buttons/disk.png")
    public ImageResource save();
    
    @Source("com/googlecode/common/client/ui/buttons/disk_disabled.png")
    public ImageResource saveDisabled();
    
    @Source("com/googlecode/common/client/ui/buttons/find.png")
    public ImageResource find();
    
    @Source("com/googlecode/common/client/ui/buttons/folder.png")
    public ImageResource folder();
    
    @Source("com/googlecode/common/client/ui/buttons/folder_disabled.png")
    public ImageResource folderDisabled();
    
    @Source("com/googlecode/common/client/ui/buttons/info.png")
    public ImageResource info();
    
    @Source("com/googlecode/common/client/ui/buttons/info_disabled.png")
    public ImageResource infoDisabled();
    
    @Source("com/googlecode/common/client/ui/buttons/printer.png")
    public ImageResource print();
    
    @Source("com/googlecode/common/client/ui/buttons/printer_disabled.png")
    public ImageResource printDisabled();
    
    @Source("com/googlecode/common/client/ui/buttons/accept.png")
    public ImageResource ok();
    
    @Source("com/googlecode/common/client/ui/buttons/accept_disabled.png")
    public ImageResource okDisabled();
    
    @Source("com/googlecode/common/client/ui/buttons/cancel.png")
    public ImageResource cancel();

    @Source("com/googlecode/common/client/ui/buttons/cancel_disabled.png")
    public ImageResource cancelDisabled();

    @Source("com/googlecode/common/client/ui/buttons/db_save.png")
    public ImageResource dbSave();

    @Source("com/googlecode/common/client/ui/buttons/db_save_disabled.png")
    public ImageResource dbSaveDisabled();

}
