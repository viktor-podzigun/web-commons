
package com.googlecode.common.client.config.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


/**
 * Defines config images.
 */
public interface ConfigImages extends ClientBundle {

    public static final ConfigImages INSTANCE = GWT.create(ConfigImages.class);
    
    
    @Source("array_selected.png")
    public ImageResource arraySelected();
    
    @Source("array.png")
    public ImageResource array();
    
    @Source("object_selected.png")
    public ImageResource objectSelected();
    
    @Source("object.png")
    public ImageResource object();
    
}
