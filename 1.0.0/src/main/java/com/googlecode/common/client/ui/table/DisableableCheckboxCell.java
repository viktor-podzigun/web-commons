package com.googlecode.common.client.ui.table;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;


public final class DisableableCheckboxCell extends CheckboxCell {

    private boolean enabled;

    
    private static final SafeHtml INPUT_CHECKED_DISABLED = SafeHtmlUtils
            .fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" "
                    + "checked disabled=\"disabled\"/>");

    private static final SafeHtml INPUT_UNCHECKED_DISABLED = SafeHtmlUtils
            .fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" "
                    + "disabled=\"disabled\"/>");

    public DisableableCheckboxCell(){
        this(true);
    }
    
    public DisableableCheckboxCell(boolean enabled) {
        super();
        this.enabled = enabled;
    }
    
    public DisableableCheckboxCell(boolean dependsOnSelection, 
            boolean handlesSelection) {
        
        super(dependsOnSelection,handlesSelection);
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
            Boolean value, SafeHtmlBuilder sb) {
        
        if (isEnabled()) {
            super.render(context, value, sb);
        } else {
            if (!enabled) {
                if (value) {
                    sb.append(INPUT_CHECKED_DISABLED);
                } else {
                    sb.append(INPUT_UNCHECKED_DISABLED);
                }
            } else {
                super.render(context, value, sb);
            }
        }
    }
}
