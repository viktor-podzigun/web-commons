
package com.googlecode.common.client.cal;

import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayUtils;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;


/**
 * Represents generic calendar panel.
 * 
 * <p>Got from here:<br>
 * <a href="https://github.com/Serhioromano/bootstrap-calendar">
 *      github.com/Serhioromano/bootstrap-calendar</a>
 */
public class CalendarPanel extends Composite {
    
    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, CalendarPanel> {
    }
    
    @UiField Element            calendarPanel;
    
    private JavaScriptObject    calendar;
    private List<CalendarEvent> events;

    
    public CalendarPanel() {
        initWidget(binder.createAndBindUi(this));
        
        calendarPanel.setId(DOM.createUniqueId());
    }
    
    @Override
    protected void onLoad() {
        super.onLoad();
        
        if (calendar == null) {
            calendar = init0(calendarPanel.getId(), 
                    GWT.getModuleBaseForStaticFiles());
            
            if (events != null) {
                setEvents(events);
            }
        }
    }
    
    public void setEvents(List<CalendarEvent> events) {
        this.events = events;
        
        if (calendar != null) {
            setEvents0(JsArrayUtils.readOnlyJsArray(events.toArray(
                    new CalendarEvent[events.size()])));
        }
    }
    
    protected void onEventClick(CalendarEvent event) {
    }
    
    /**
     * 
     * @param startDate     
     * @param currentView   possible values: 'year', 'month', 'week', 'day'
     */
    protected void onCreateEvent(String startDate, String currentView) {
    }
    
    private native void setEvents0(JsArray<CalendarEvent> events) /*-{
        var calendar = 
            this.@com.googlecode.common.client.cal.CalendarPanel::calendar;
        
        calendar.setOptions({
            events_source: events
        });
        
        calendar.view();
    }-*/;
    
    private native JavaScriptObject init0(String id, String resBase) /*-{
        var $ = $wnd.jQuery;
        var that = this;
        var options = {
            events_source: [],
            view: 'week',
            time_start:         '00:00',
            time_end:           '23:59',
            tmpl_path: resBase + 'cal/tmpls/',
            tmpl_cache: true,
            //day: '2013-03-12',
            onAfterViewLoad: function(view) {
                $('.page-header h4').text(this.getTitle());
                $('.btn-group button').removeClass('active');
                $('button[data-calendar-view="' + view + '"]').addClass(
                    'active');
            },
            classes: {
                months: {
                    general: 'label'
                }
            }
        };
        
        var calendar = $("#" + id).calendar(options);
        
        $('.btn-group button[data-calendar-nav]').each(function() {
            var $this = $(this);
            $this.click(function() {
                calendar.navigate($this.data('calendar-nav'));
            });
        });
    
        $('.btn-group button[data-calendar-view]').each(function() {
            var $this = $(this);
            $this.click(function() {
                calendar.view($this.data('calendar-view'));
            });
        });
        
        calendar.onDblClick = function(startDate, view) {
            that.@com.googlecode.common.client.cal.CalendarPanel::onCreateEvent(Ljava/lang/String;Ljava/lang/String;)(startDate,view);
        };
    
        calendar._update = function() {
            var self = this;
            $('*[data-toggle="tooltip"]').tooltip({container: 'body'});
            $('*[data-cal-date]').click(function() {
                var view = $(this).data('cal-view');
                self.options.day = $(this).data('cal-date');
                self.view(view);
            });
            if ($('.cal-cell').length > 0) {
                $('.cal-cell').dblclick(function() {
                    self.onDblClick($('[data-cal-date]', this).data('cal-date'), self.options.view);
                });
            } else {
                $('.cal-cell1').dblclick(function(e) {
                    self.onDblClick($('[data-cal-date]', this).data('cal-date'), self.options.view);
                });
            }
            this['_update_' + this.options.view]();
            this._update_modal();
        };
    
        calendar._update_modal = function() {
            $('a[data-event-id]', calendar.context).unbind('click');
            $('a[data-event-id]', calendar.context).on('click', function(e) {
                var target = e.currentTarget;
                $(target).trigger('mouseout');
                e.preventDefault();
                e.stopPropagation();
    
                var id = $(this).data("event-id");
                var event = $wnd._.find(calendar.options.events, function(event) {
                    return event.id == id;
                });
                
                that.@com.googlecode.common.client.cal.CalendarPanel::onEventClick(Lcom/googlecode/common/client/cal/CalendarEvent;)(event);
            });
        };
        
        return calendar;
    }-*/;

}
