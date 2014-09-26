
package com.googlecode.common.client.datepicker;

import java.util.Date;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.StateButton;
import com.googlecode.common.client.util.DateHelpers;


public final class SimpleDatePicker extends Composite implements 
        HasChangeHandlers, HasBlurHandlers {
    
    public static enum Type {
       DATE_AND_TIME,
       DATE_ONLY,
       TIME_ONLY
    }
    
    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, SimpleDatePicker> {
    }
    
    private final String    pickerWidgetId;
    private final Type      type;
    
    private boolean             loaded;
    private HandlerRegistration hndlrReg;
    private Date                dateBeforeLoad;
    
    @UiField HTMLPanel      picker;
    @UiField StateButton    button;
    @UiField TextBox        textBox;

    
    public SimpleDatePicker() {
        this(Type.DATE_AND_TIME, true);
    }
    
    public SimpleDatePicker(boolean enabled) {
        this(Type.DATE_AND_TIME, enabled);
    }
    
    public SimpleDatePicker(Type type, boolean enabled) {
        this.type = type;
        
        initWidget(binder.createAndBindUi(this));

        textBox.setStyleName("input-medium");
        textBox.addDomHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                fireChangeEvent();
            }
        }, ChangeEvent.getType());
        
        picker.getElement().setId(DOM.createUniqueId());
        pickerWidgetId = DOM.createUniqueId();
        
        button.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE 
                        && button.getValue()) {
                    
                    hide();
                }
            }
        });
        
        setEnabled(enabled);
    }
    
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return addHandler(handler, ChangeEvent.getType());
    }
    
    @Override
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return textBox.addBlurHandler(handler);
    }
    
    @Override
    public void addStyleName(String style) {
        textBox.addStyleName(style);
    }
    
    @Override
    public void removeStyleName(String style) {
        textBox.removeStyleName(style);
    }
    
    @Override
    protected void onLoad() {
        super.onLoad();
        loaded = true;
        String id = picker.getElement().getId(); 
        
        if (type == null || type.equals(Type.DATE_AND_TIME)) {
            _init("yyyy-MM-dd hh:mm:ss", id, pickerWidgetId, true, true);
            button.setGlyphIcon("icon-calendar");
        
        } else if (type.equals(Type.DATE_ONLY)) {
            _init("yyyy-MM-dd", id, pickerWidgetId, true, false);
            button.setGlyphIcon("icon-calendar");
        
        } else if (type.equals(Type.TIME_ONLY)) {
            _init("hh:mm:ss", id, pickerWidgetId, false, true);
            button.setGlyphIcon("icon-time");
        }
        
        if (dateBeforeLoad != null) {
            setDate(dateBeforeLoad);
            dateBeforeLoad = null;
        }
        
        hndlrReg = Event.addNativePreviewHandler(new NativePreviewHandler() {
            @Override
            public void onPreviewNativeEvent(NativePreviewEvent event) {
                if (!button.getValue()) {
                    // button is not pressed
                    return;
                }
                
                Event nativeEvent = Event.as(event.getNativeEvent());
                EventTarget target = nativeEvent.getEventTarget();
                int type = nativeEvent.getTypeInt();
                
                if (type == Event.ONCLICK || type == Event.ONDBLCLICK 
                        || type == Event.ONCONTEXTMENU) {
                    
                    if (Element.is(target) 
                            && !(DOM.getElementById(pickerWidgetId)
                                    .isOrHasChild(Element.as(target)) 
                            || button.getElement().isOrHasChild(Element
                                    .as(target)))) {
                        
                        hide();
                    }
                }
            }
        });
    }
    
    @Override
    protected void onUnload() {
        hndlrReg.removeHandler();
        _destroy(picker.getElement().getId());
        loaded = false;
        
        super.onUnload();
    }
    
    protected void fireChangeEvent() {
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), this);
    }
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
        textBox.setEnabled(enabled);
    }
    
    public boolean isEnabled() {
        return button.isEnabled();
    }

    /**
     * Returns defined date
     * 
     * @return  Date if it was defined, null otherwise. 
     */
    public Date getDate() {
        String dateStr = textBox.getText().trim();
        if (!dateStr.isEmpty()) {
            if (type == Type.DATE_AND_TIME || type == Type.DATE_ONLY) {
                return DateHelpers.parseDate(dateStr);
            }
        }
        
        return null;
    }
    
    /**
     * Returns defined date using UTC timezone
     * 
     * @return  Date UTC if it was defined, null otherwise. 
     */
    public Date getDateUTC() {
        String dateStr = textBox.getText().trim();
        if (!dateStr.isEmpty()) {
            if (type == Type.DATE_AND_TIME || type == Type.DATE_ONLY) {
                return DateHelpers.utcParseDate(dateStr);
            }
        }
        
        return null;
    }
    
    /**
     * Returns time period in seconds 
     * 
     * @return time period in seconds if it was defined, null otherwise.
     */
    public Long getTimePeriod() {
        String timeStr = textBox.getText().trim();
        
        if (type == Type.TIME_ONLY & !timeStr.isEmpty()) {
            String[] splited = timeStr.split(":");
            
            if (splited.length == 3) {
                long hr = Long.parseLong(splited[0]);
                long min = Long.parseLong(splited[1]);
                long sec = Long.parseLong(splited[2]);
                
                return hr * 3600L + min * 60L + sec;
            }
        }
        
        return null; 
    }

    public void setDate(Date newDate) {
        if (loaded) {
            if (newDate != null) {
                _setDate(picker.getElement().getId(), newDate.getTime());
            } else {
                textBox.setText("");
            }
        } else {
            dateBeforeLoad = newDate;
        }
    }
    
    public void setDateUTC(Date date) {
        Date newDate = new Date();
        
        @SuppressWarnings("deprecation")
        int timeZoneOffset = newDate.getTimezoneOffset();
        
        newDate.setTime(date.getTime() + timeZoneOffset * 60L * 1000L);
        setDate(newDate);
    }
    
    private void show() {
        _show0(picker.getElement().getId());
    }
    
    private void hide() {
        _hide0(picker.getElement().getId());
    }
    
    private void onShow(String id) {
        button.setValue(true);
    }
    
    private void onHide(String id) {
        button.setValue(false);
    }
   
    @UiHandler("button")
    void onClick(ClickEvent event) {
        if (button.getValue()) {
            show();
        } else {
            hide();
        }
    }

    private native void _init(String format, String id, String newId, 
            boolean pickDate, boolean pickTime) /*-{
        
        var that = this;
        var el = $wnd.$('#' + id);
        el.datetimepicker({
             format: format,
             pickDate: pickDate,
             pickTime: pickTime,
             newId : newId,
             pickerId : id
        });

//        el.data('datetimepicker').set(); // fill the text field
        
        el.on( "show", function() {
            that.@com.googlecode.common.client.datepicker.SimpleDatePicker::onShow(Ljava/lang/String;)(id)
        });
        
        el.on( "hide", function() {
            that.@com.googlecode.common.client.datepicker.SimpleDatePicker::onHide(Ljava/lang/String;)(id)
        });
        
    }-*/;
    
    private native void _destroy(String id) /*-{
        $wnd.$('#' + id).data('datetimepicker').destroy();
    }-*/;
    
//    private static native double _getDate(String id) /*-{
//        return $wnd.$('#' + id).data('datetimepicker')
//                .getLocalDate().getTime();
//    }-*/;
    
    private static native void _setDate(String id, double newDate) /*-{
        return $wnd.$('#' + id).data('datetimepicker')
                .setLocalDate(new Date(newDate));
    }-*/;
    
    private static native void _show0(String id) /*-{
        $wnd.$('#' + id).data('datetimepicker').show();
    }-*/;
    
    private static native void _hide0(String id) /*-{
        $wnd.$('#' + id).data('datetimepicker').hide();
    }-*/;

}
