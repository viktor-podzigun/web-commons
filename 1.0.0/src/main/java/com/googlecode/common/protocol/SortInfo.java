
package com.googlecode.common.protocol;


/**
 * Part of server requests containing data sorting info.
 */
public final class SortInfo {

    private String  column;
    private boolean ascending;

    
    public SortInfo() {
    }

    public SortInfo(String column, boolean ascending) {
        this.column    = column;
        this.ascending = ascending;
    }
    
    public String getColumn() {
        return column;
    }
    
    public void setColumn(String column) {
        this.column = column;
    }
    
    public boolean isAscending() {
        return ascending;
    }
    
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

}
