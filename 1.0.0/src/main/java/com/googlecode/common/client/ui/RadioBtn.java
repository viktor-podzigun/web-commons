
package com.googlecode.common.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.RadioButton;


/**
 * Fixed RadioButton 
 */
public class RadioBtn extends RadioButton {

    private boolean loaded;
    private boolean value;
    private boolean fireEvents;
    
    
    public RadioBtn() {
        this("");
    }
    
    public RadioBtn(String name) {
        super(name);
        
        Style spanStyle = getElement().getFirstChildElement()
                .getNextSiblingElement().getStyle();
        
        spanStyle.setDisplay(Display.INLINE_BLOCK);
        spanStyle.setPaddingLeft(5, Unit.PX);
        spanStyle.setMarginRight(15, Unit.PX);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        
        if (!loaded) {
            loaded = true;
            getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
            setValue(value, fireEvents);
        }
    }
    
    @Override
    protected void onUnload() {
        super.onUnload();
    }
    
    @Override
    public void setValue(Boolean value) {
        this.value = value;
        this.fireEvents = false;
        
        if (loaded) {
            super.setValue(value);
        }
    }
    
    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        this.value = value;
        this.fireEvents = fireEvents;
        
        if (loaded) {
            super.setValue(value, fireEvents);
        }
    }
    
}
