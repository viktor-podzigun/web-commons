
package com.googlecode.common.client.ui.list;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.googlecode.common.client.ui.ImageTextCell;
import com.googlecode.common.client.ui.PickList;


/**
 * Default implementation for {@link PickList.Item}.
 */
public class DefaultListItem implements PickList.Item {
    
    public static final Cell<DefaultListItem>   CELL = new ItemCell();
    
    private final String        title;
    private final boolean       isMovable;
    private final ImageResource image;

    
    public DefaultListItem(String title) {
        this(title, true);
    }
    
    public DefaultListItem(String title, boolean isMovable) {
        this(title, isMovable, null);
    }
    
    public DefaultListItem(String title, boolean isMovable, 
            ImageResource image) {
        
        this.title      = title;
        this.isMovable  = isMovable;
        this.image      = image;
    }
    
    @Override
    public boolean isMovable() {
        return isMovable;
    }
    
    public ImageResource getImage() {
        return image;
    }
    
    @Override
    public String toString() {
        return title;
    }

    
    private static class ItemCell extends ImageTextCell<DefaultListItem> {
        
        private ItemCell() {
        }
        
        @Override
        public void render(Context c, DefaultListItem value, 
                SafeHtmlBuilder sb) {
            
            setImage(value.getImage());
            super.render(c, value, sb);
        }
    }

}
