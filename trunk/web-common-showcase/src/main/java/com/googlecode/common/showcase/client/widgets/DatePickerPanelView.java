
package com.googlecode.common.showcase.client.widgets;

import java.util.Date;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.datepicker.SimpleDatePicker;
import com.googlecode.common.client.ui.TextField;
import com.googlecode.common.client.ui.panel.ErrorPanel;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.util.DateHelpers;
import com.googlecode.common.showcase.client.AbstractPanelView;


public final class DatePickerPanelView extends AbstractPanelView {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, DatePickerPanelView> {
    }
    
    @UiField(provided=true) SimpleDatePicker    picker1;
    @UiField(provided=true) SimpleDatePicker    picker2;
    @UiField(provided=true) SimpleDatePicker    picker3;
    @UiField TextField    dateField;

    
    public DatePickerPanelView() {
        picker1 = new SimpleDatePicker();
        picker2 = new SimpleDatePicker(SimpleDatePicker.Type.DATE_ONLY);
        picker3 = new SimpleDatePicker(SimpleDatePicker.Type.TIME_ONLY);
        
        initWidget(binder.createAndBindUi(this));
        dateField.setText(DateHelpers.formatDate(new Date()));
    }
    
    @UiHandler("btn1GetDate")
    void onBtn1GetDate(ClickEvent event) {
        showDateMsg(picker1.getDate());
    }
    
    @UiHandler("btn1GetUtcDate")
    void onBtn1GetUtcDate(ClickEvent event) {
        showDateMsg(picker1.getDateUTC());
    }
    
    @UiHandler("btnSetDate")
    void onBtnSetDate(ClickEvent event) {
        try {
            picker1.setDate(DateHelpers.parseDate(dateField.getText()));
        } catch (IllegalArgumentException ex) {
            ErrorPanel.show("Date format must be yyyy-MM-dd HH:mm:ss");
        }
    }
    
    @UiHandler("btn2GetDate")
    void onBtn2GetDate(ClickEvent event) {
        showDateMsg(picker2.getDate());
    }
    
    @UiHandler("btn2GetUtcDate")
    void onBtn2GetUtcDate(ClickEvent event) {
        showDateMsg(picker2.getDateUTC());
    }
    
    @UiHandler("btn3GetTimePeriod")
    void onBtn3GetTimePeriod(ClickEvent event) {
        Long timePeriod = picker3.getTimePeriod();
        
        if (timePeriod != null) {
            MessageBox.showMessage("Time period: " + timePeriod + " sec");
        } else {
            MessageBox.showMessage("Time is not defined");
        }
    }
    
    private void showDateMsg(Date date) {
        if (date != null) {
            String msg = "User timezone date: " + DateHelpers.formatDate(date) 
                    + "\nUTC timezone date: " + DateHelpers.utcFormatDate(date);
            MessageBox.showMessage(msg);
        } else {
            MessageBox.showMessage("Date is not defined");
        }
    }
    
}
