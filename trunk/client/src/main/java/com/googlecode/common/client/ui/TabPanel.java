
package com.googlecode.common.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.util.StringHelpers;


/**
 * Wraps Bootstrap tab functionality.
 */
public class TabPanel extends Composite implements HasWidgets.ForIsWidget, 
        IndexedPanel.ForIsWidget, HasBeforeSelectionHandlers<Integer>, 
        HasSelectionHandlers<Integer> {

    public static enum Direction {
        TOP, BOTTOM, LEFT, RIGHT
    }
    
    private final String            tabPanelId;
    private final HTMLPanel         tabPanel;
    private final List<MenuItem>    tabs    = new ArrayList<MenuItem>();
    private final FlowPanel         content = new FlowPanel();
    
    private int                     selectedIndex = -1;

    
    public TabPanel() {
        this(Direction.TOP);
    }
    
    public TabPanel(Direction direction) {
        FlowPanel panel = new FlowPanel();
        panel.setStyleName(getDirectionStyle(direction));
        initWidget(panel);

        tabPanelId = DOM.createUniqueId();
        tabPanel = new HTMLPanel("ul", "");
        tabPanel.setStyleName("nav nav-tabs");
        tabPanel.getElement().setId(tabPanelId);
        
        content.setStyleName("tab-content");
        
        if (direction == Direction.BOTTOM) {
            panel.add(content);
            panel.add(tabPanel);
        } else {
            panel.add(tabPanel);
            panel.add(content);
        }
    }
    
    private String getDirectionStyle(Direction direction) {
        switch (direction) {
        case TOP:       return "tabbable";
        case BOTTOM:    return "tabbable tabs-below";
        case LEFT:      return "tabbable tabs-left";
        case RIGHT:     return "tabbable tabs-right";
        
        default:
            throw new IllegalArgumentException("Unknown direction: " 
                    + direction);
        }
    }
    
    @Override
    public HandlerRegistration addBeforeSelectionHandler(
            BeforeSelectionHandler<Integer> handler) {
        
        return addHandler(handler, BeforeSelectionEvent.getType());
    }

    @Override
    public HandlerRegistration addSelectionHandler(
            SelectionHandler<Integer> handler) {
        
        return addHandler(handler, SelectionEvent.getType());
    }

    /**
     * Gets the index of the currently-selected tab.
     *
     * @return the selected index, or <code>-1</code> if none is selected.
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Returns the widget at the given index.
     */
    @Override
    public Widget getWidget(int index) {
        return content.getWidget(index);
    }

    /**
     * Returns the number of tabs and widgets.
     */
    @Override
    public int getWidgetCount() {
        return content.getWidgetCount();
    }

    /**
     * Convenience overload to allow {@link IsWidget} to be used directly.
     */
    @Override
    public int getWidgetIndex(IsWidget child) {
        return getWidgetIndex(asWidgetOrNull(child));
    }

    /**
     * Returns the index of the given child, or -1 if it is not a child.
     */
    @Override
    public int getWidgetIndex(Widget child) {
        return content.getWidgetIndex(child);
    }

    @Override
    public void add(IsWidget w) {
        add(asWidgetOrNull(w));
    }
    
    @Override
    public void add(Widget w) {
        add(w, "");
    }
    
    public void add(Widget child, String text) {
        add(child, text, null);
    }

    public void add(Widget child, String text, ImageResource image) {
        insert(child, text, image, getWidgetCount());
    }
    
    /**
     * Convenience overload to allow {@link IsWidget} to be used directly.
     */
    public void insert(IsWidget child, String text, int beforeIndex) {
        insert(asWidgetOrNull(child), text, beforeIndex);
    }
    
    public void insert(Widget child, String text, ImageResource image, 
            int beforeIndex) {
        
        insert(child, text, beforeIndex);
        
        if (image != null) {
            tabs.get(beforeIndex).setImage(image);
        }
    }

    /**
     * Sets a tab's text.
     * 
     * @param index the index of the tab whose text is to be set
     * @param text  text to be set
     */
    public void setTabText(int index, String text) {
        checkIndex(index);
        tabs.get(index).setText(text);
    }
    
    /**
     * Sets a tab's styleName.
     * 
     * @param index the index of the tab whose styleName is to be set
     * @param style style to be set
     */
    public void setTabStyleName(int index, String style) {
        checkIndex(index);
        tabs.get(index).setStyleName(style);
    }
    
    /**
     * Sets a tab's image.
     * 
     * @param index the index of the tab whose image is to be set
     * @param image image to be set
     */
    public void setTabImage(int index, ImageResource image) {
        checkIndex(index);
        tabs.get(index).setImage(image);
    }
    
    /**
     * Inserts a widget into the panel. If the Widget is already attached, 
     * it will be moved to the requested index.
     *
     * @param child         the widget to be added
     * @param text          the text to be shown on its tab
     * @param beforeIndex   the index before which it will be inserted
     */
    public void insert(final Widget child, String text, int beforeIndex) {
        // Check to see if the TabPanel already contains the Widget. If so, 
        // remove it and see if we need to shift the position to the left.
        int idx = getWidgetIndex(child);
        if (idx != -1) {
            remove(child);
            if (idx < beforeIndex) {
                beforeIndex--;
            }
        }

        //final int index = beforeIndex;
        String targetId = child.getElement().getId();
        if (StringHelpers.isNullOrEmpty(targetId)) {
            targetId = DOM.createUniqueId();
            child.getElement().setId(targetId);
        }
        
        child.removeStyleName("active");
        child.addStyleName("tab-pane");
        
        MenuItem tabWidget = new MenuItem(text, new Command() {
            @Override
            public void execute() {
                selectTab(getWidgetIndex(child));
            }
        });
        tabWidget.setTargetId(targetId);
        
        tabs.add(beforeIndex, tabWidget);
        content.insert(child, beforeIndex);
    
        if (beforeIndex < getWidgetCount()) {
            // we insert in the middle, rearrange widgets in tabPanel
            tabPanel.clear();
            for (MenuItem tab : tabs) {
                tabPanel.add(tab, tabPanelId);
            }
        } else {
            tabPanel.add(tabWidget, tabPanelId);
        }
        
        if (selectedIndex == -1) {
            selectTab(0);
        } else if (selectedIndex >= beforeIndex) {
            // if we inserted before the currently selected tab,
            // its index has just increased
            selectedIndex++;
        }
    }

    @Override
    public Iterator<Widget> iterator() {
        return content.iterator();
    }

    @Override
    public boolean remove(int index) {
        if (index < 0 || index >= getWidgetCount()) {
            return false;
        }

        MenuItem tabWidget = tabs.get(index);
        tabs.remove(index);
        tabPanel.remove(tabWidget);
        content.remove(index);

        if (index == selectedIndex) {
            // if the selected tab is being removed, select the next tab 
            // (if there is one)
            selectedIndex = -1;
            int count = getWidgetCount();
            if (index == count) {
                index = count - 1;
            }
            
            if (index != -1) {
                selectTab(index);
            }
        } else if (index < selectedIndex) {
            // if the selectedIndex is greater than the one being removed, 
            // it needs to be adjusted
            selectedIndex--;
        }
        
        return true;
    }

    @Override
    public boolean remove(IsWidget w) {
        return remove(asWidgetOrNull(w));
    }
    
    @Override
    public boolean remove(Widget w) {
        int index = getWidgetIndex(w);
        if (index == -1) {
            return false;
        }

        return remove(index);
    }

    @Override
    public void clear() {
        Iterator<Widget> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    /**
     * Programmatically selects the specified tab and fires events.
     *
     * @param index the index of the tab to be selected
     */
    public void selectTab(int index) {
        selectTab(index, true);
    }

    /**
     * Programmatically selects the specified tab.
     *
     * @param index the index of the tab to be selected
     * @param fireEvents true to fire events, false not to
     */
    public void selectTab(int index, boolean fireEvents) {
        checkIndex(index);
        if (index == selectedIndex) {
            return;
        }

        // fire the before selection event, giving the recipients a chance to
        // cancel the selection
        if (fireEvents) {
            BeforeSelectionEvent<Integer> event = BeforeSelectionEvent.fire(
                    this, index);
            if ((event != null) && event.isCanceled()) {
                return;
            }
        }

        selectedIndex = index;
        showTab0(tabPanelId, index);

        // fire the selection event
        if (fireEvents) {
            SelectionEvent.fire(this, index);
        }
    }

    /**
     * Convenience overload to allow {@link IsWidget} to be used directly.
     */
    public void selectTab(IsWidget child) {
        selectTab(asWidgetOrNull(child));
    }

    /**
     * Convenience overload to allow {@link IsWidget} to be used directly.
     */
    public void selectTab(IsWidget child, boolean fireEvents) {
        selectTab(asWidgetOrNull(child), fireEvents);
    }

    /**
     * Programmatically selects the specified tab and fires events.
     * 
     * @param child
     *            the child whose tab is to be selected
     */
    public void selectTab(Widget child) {
        selectTab(getWidgetIndex(child));
    }

    /**
     * Programmatically selects the specified tab.
     * 
     * @param child         the child whose tab is to be selected
     * @param fireEvents    true to fire events, false not to
     */
    public void selectTab(Widget child, boolean fireEvents) {
        selectTab(getWidgetIndex(child), fireEvents);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= getWidgetCount()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        
        // make selected tab active
        if (selectedIndex != -1) {
            showTab0(tabPanelId, selectedIndex);
        }
    }
    
    protected native void showTab0(String id, int index) /*-{
        $wnd.jQuery("#" + id + " li:eq(" + index + ") a").tab('show');
    }-*/;

}
