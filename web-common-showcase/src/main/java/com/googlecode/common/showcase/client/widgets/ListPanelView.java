
package com.googlecode.common.showcase.client.widgets;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.PickList;
import com.googlecode.common.client.ui.list.DefaultListItem;
import com.googlecode.common.showcase.client.AbstractPanelView;


/**
 * {@link PickList} show-case.
 */
public final class ListPanelView extends AbstractPanelView {

    private static final Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, ListPanelView> {
    }
    
    @UiField(provided=true) PickList<DefaultListItem>   pickList;


    public ListPanelView() {
        List<DefaultListItem> srcList = new ArrayList<DefaultListItem>();
        srcList.add(new DefaultListItem("Test item 1", true, 
                ButtonImages.INSTANCE.ok()));
        srcList.add(new DefaultListItem("Test item 2"));
        srcList.add(new DefaultListItem("Test item 3", true, 
                ButtonImages.INSTANCE.ok()));
        
        List<DefaultListItem> dstList = new ArrayList<DefaultListItem>();
        dstList.add(new DefaultListItem("Test item 4", false, 
                ButtonImages.INSTANCE.okDisabled()));
        dstList.add(new DefaultListItem("Test item 5", false, 
                ButtonImages.INSTANCE.okDisabled()));
        
        pickList = new PickList<DefaultListItem>(DefaultListItem.CELL, 
                srcList, dstList);
        
        initWidget(binder.createAndBindUi(this));
    }

}
