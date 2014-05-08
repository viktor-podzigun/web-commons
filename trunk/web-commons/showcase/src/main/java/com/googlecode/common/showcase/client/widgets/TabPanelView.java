
package com.googlecode.common.showcase.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.TabPanel;
import com.googlecode.common.client.ui.TabPanel.Direction;
import com.googlecode.common.showcase.client.AbstractPanelView;


/**
 * {@link TabPanel} show-case.
 */
public final class TabPanelView extends AbstractPanelView {

    private static final Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, TabPanelView> {
    }
    
    @UiField(provided=true) TabPanel    tabTop;
    @UiField(provided=true) TabPanel    tabLeft;
    @UiField(provided=true) TabPanel    tabBottom;


    public TabPanelView() {
        tabTop    = createTabPanel(Direction.TOP);
        tabLeft   = createTabPanel(Direction.LEFT);
        tabBottom = createTabPanel(Direction.BOTTOM);
        
        initWidget(binder.createAndBindUi(this));
        
        tabTop.add(new HTML("<p>Content for tab 4</p>"), "Tab 4");
        tabTop.add(new HTML("<p>Content for tab 5</p>"), "Tab 5");
        
        tabTop.selectTab(4);
        tabTop.remove(3);
        tabTop.remove(3);
    }
    
    private TabPanel createTabPanel(Direction direction) {
        TabPanel tab = new TabPanel(direction);
        tab.add(new HTML("<p>Content for tab 1</p>"), "Tab 1");
        
        tab.add(new HTML("<p>Content for tab 2</p>"));
        tab.setTabText(1, "Tab 2");
        tab.setTabImage(1, ButtonImages.INSTANCE.find());
        
        tab.add(new HTML("<p>Content for tab 3</p>"), "Tab 3");
        return tab;
    }

}
