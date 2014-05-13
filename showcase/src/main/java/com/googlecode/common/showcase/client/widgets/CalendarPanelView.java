
package com.googlecode.common.showcase.client.widgets;

import java.util.Arrays;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.cal.CalendarEvent;
import com.googlecode.common.client.cal.CalendarEventType;
import com.googlecode.common.client.cal.CalendarPanel;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.showcase.client.AbstractPanelView;


/**
 * {@link CalendarPanel} show-case.
 */
public final class CalendarPanelView extends AbstractPanelView {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, CalendarPanelView> {
    }
    
    @UiField(provided=true) CalendarPanel   calPanel;


    public CalendarPanelView() {
        calPanel = new CalendarPanel() {
            @Override
            protected void onEventClick(CalendarEvent event) {
                MessageBox.showMessage(String.valueOf(event.getData()));
            }
        };
        
        long currTime = System.currentTimeMillis();
        
        calPanel.setEvents(Arrays.asList(
                CalendarEvent.create("292", 
                        "Event 2", CalendarEventType.INFO, 
                        currTime, currTime + (60 * 60 * 1000), 
                        "test"), 
                CalendarEvent.create("293", 
                        "Event 3", CalendarEventType.SPECIAL, 
                        currTime, currTime + (2 * 60 * 60 * 1000), 
                        "test2")));
        
        initWidget(binder.createAndBindUi(this));
    }
    
}
