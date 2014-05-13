
package com.googlecode.common.client.ui.table;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;


/**
 * Generic table column.
 */
public abstract class TableColumn<T> extends Column<T, String> {

    
    public TableColumn() {
        this(null);
    }
    
    public TableColumn(Enum<?> sortInfo) {
        this(sortInfo, true);
    }
    
    public TableColumn(Enum<?> sortInfo, boolean isAscending) {
        super(new TextCell());
        
        if (sortInfo != null) {
            setSortable(true);
            setDataStoreName(sortInfo.name());
            setDefaultSortAscending(isAscending);
        }
    }

}
