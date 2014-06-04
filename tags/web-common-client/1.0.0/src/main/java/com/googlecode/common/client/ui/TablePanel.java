
package com.googlecode.common.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.view.client.ProvidesKey;


public class TablePanel<T> extends CellTable<T> {

    /** The default page size */
    private static final int DEFAULT_PAGESIZE = 15;

    
    /**
     * Constructs a table with a default page size of 15.
     */
    public TablePanel() {
        this(DEFAULT_PAGESIZE);
    }

    /**
     * Constructs a table with the given page size.
     * 
     * @param pageSize  the page size
     */
    public TablePanel(int pageSize) {
        this(pageSize, null);
    }

    /**
     * Constructs a table with a default page size of 15, and the given
     * {@link ProvidesKey key provider}.
     * 
     * @param keyProvider   an instance of ProvidesKey<T>, or null if 
     *                      the record object should act as its own key
     */
    public TablePanel(ProvidesKey<T> keyProvider) {
        this(DEFAULT_PAGESIZE, keyProvider);
    }

    /**
     * Constructs a table with the given page size, and the given key provider.
     * 
     * @param pageSize      the page size
     * @param keyProvider   an instance of ProvidesKey<T>, or null if 
     *                      the record object should act as its own key
     */
    public TablePanel(int pageSize, ProvidesKey<T> keyProvider) {
        super(pageSize, TableResources.INSTANCE, keyProvider);
        
        setStyleName("table table-condensed");
    }

    
    interface TableResources extends Resources {
        
        static final TableResources INSTANCE = GWT.create(TableResources.class);

        @Source("com/googlecode/common/client/ui/TablePanel.css")
        Style cellTableStyle();
    }
    
}
