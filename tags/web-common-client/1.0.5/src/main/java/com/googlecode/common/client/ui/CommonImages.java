
package com.googlecode.common.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


/**
 * Defines common client images.
 */
public interface CommonImages extends ClientBundle {

    public static final CommonImages INSTANCE = GWT.create(CommonImages.class);
    
    
    @Source("loading.gif")
    public ImageResource loading();

    @Source("icons/dialog-error.png")
    public ImageResource dialogError();

    @Source("icons/dialog-information.png")
    public ImageResource dialogInfo();

    @Source("icons/dialog-question.png")
    public ImageResource dialogQuestion();

    @Source("icons/dialog-warning.png")
    public ImageResource dialogWarning();

}
