package com.googlecode.common.client.ui.table;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.user.cellview.client.Column;


public abstract class CheckboxColumn<T> extends Column<T, Boolean> {

    public CheckboxColumn() {
        super(new CheckboxCell());
    }
    
    public CheckboxColumn(CheckboxCell cell) {
        super(cell);
    }

}
