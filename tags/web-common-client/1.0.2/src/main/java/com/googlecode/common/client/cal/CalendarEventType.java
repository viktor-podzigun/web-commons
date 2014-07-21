
package com.googlecode.common.client.cal;


/**
 * Defines calendar event types.
 */
public enum CalendarEventType {
    
    IMPORTANT("event-important"),
    WARNING("event-warning"),
    INFO("event-info"),
    INVERSE("event-inverse"),
    SUCCESS("event-success"),
    SPECIAL("event-special"),
    ;
    
    private final String    styleName;
    
    
    private CalendarEventType(String styleName) {
        this.styleName = styleName;
    }
    
    public String getStyleName() {
        return styleName;
    }

}
