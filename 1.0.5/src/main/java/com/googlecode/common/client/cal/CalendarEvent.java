
package com.googlecode.common.client.cal;

import com.google.gwt.core.client.JavaScriptObject;


/**
 * Calendar event info.
 */
public class CalendarEvent extends JavaScriptObject {

    
    protected CalendarEvent() {
    }

    public static CalendarEvent create(String id, String title, 
            CalendarEventType type, long start, long end, Object data) {
        
        return create0(id, title, type != null ? type.getStyleName() : "", 
                start, end, data);
    }
    
    private static native CalendarEvent create0(String id, String title, 
            String styleName, double start, double end, Object data) /*-{
        
        return {
            "id":       id,
            "title":    title,
            "url":      "#",
            "class":    styleName,
            "start":    start,
            "end":      end,
            "data":     data
        };
    }-*/;
    
    public final native String getId() /*-{
        return this.id;
    }-*/;
    
    public final native String getTitle() /*-{
        return this.title;
    }-*/;

    public final native Object getData() /*-{
        return this.data;
    }-*/;

}
