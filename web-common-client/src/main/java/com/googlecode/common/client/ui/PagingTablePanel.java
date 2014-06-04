
package com.googlecode.common.client.ui;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.googlecode.common.protocol.SortInfo;


/**
 * Panel with table and paging control.
 */
public abstract class PagingTablePanel<T> extends Composite {

    public static final int     DEFAULT_PAGE_SIZE = 10;
    
    private static final Binder binder = GWT.create(Binder.class);
    @SuppressWarnings("rawtypes")
    interface Binder extends UiBinder<Widget, PagingTablePanel> {
    }

    @UiField(provided=true)
    protected TablePanel<T>     table;
    
    @UiField(provided=true)
    SimplePager                 pager;
    
    private final ListDataProvider<T> tableData = 
        new ListDataProvider<T>();
    
    private final SingleSelectionModel<T> tableSelModel = 
        new SingleSelectionModel<T>();
    
    private final HasRowsImpl   rowsImpl;
    
    private final boolean       exactRowCount;

    
    public PagingTablePanel() {
        this(true);
    }
    
    public PagingTablePanel(boolean exactRowCount) {
        this.exactRowCount = exactRowCount;
        
        table    = new TablePanel<T>();
        rowsImpl = new HasRowsImpl();
        pager    = new SimplePager(TextLocation.CENTER, false, exactRowCount) {
            /**
             * Method overrided because SimplePager have a bug with displaying 
             * last page. 
             * See <a href="http://code.google.com/p/google-web-toolkit/issues/detail?id=6163">Source</a>
             */
            @Override
            public void setPageStart(int index) {
                HasRows display = getDisplay();

                if (display != null) {
                    Range range = display.getVisibleRange();
                    int pageSize = range.getLength();
                    index = Math.max(0, index);
                    if (index != range.getStart()) {
                        display.setVisibleRange(index, pageSize);
                    }
                }
            }
        };

        pager.setDisplay(rowsImpl);
        pager.setPageSize(DEFAULT_PAGE_SIZE);
        
        initWidget(binder.createAndBindUi(this));
        
        // connect the table to the data provider
        tableData.addDataDisplay(table);
        
        table.setSelectionModel(tableSelModel);
        tableSelModel.addSelectionChangeHandler(new Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                onItemSelected(tableSelModel.getSelectedObject());
            }
        });
        
        // handler for triggering data request when sorting is changed
        table.getColumnSortList().setLimit(1);
        table.addColumnSortHandler(new ColumnSortEvent.Handler() {
            @Override
            public void onColumnSort(ColumnSortEvent event) {
                rowsImpl.setVisibleRange(rowsImpl.getVisibleRange());
            }
        });
    }
    
    public void setSelectionEventManager(DefaultSelectionEventManager<T> manager) {
        table.setSelectionModel(tableSelModel, manager);
    } 
    
    public T getSelectedItem() {
        return tableSelModel.getSelectedObject();
    }

    public void setSelected(T dto, boolean selected) {
        tableSelModel.setSelected(dto, selected);
    }
    
    protected void onItemSelected(T dto) {
    }
    
    public List<T> getList() {
        return tableData.getList();
    }

    public void setList(List<T> list) {
        final int oldCount = tableData.getList().size();
        final int newCount = list.size();
        
        // update visual data
        tableData.setList(list);
    
        // update pager data count
        if (newCount != oldCount) {
            int delta = newCount - oldCount;
            rowsImpl.setRowCount(rowsImpl.getRowCount() + delta);
        }
    }
    
    public List<SortInfo> getSortInfo() {
        List<SortInfo> sortInfo = null;
        ColumnSortList sortList = table.getColumnSortList();
        final int sortCount = sortList.size();
        if (sortCount > 0) {
            sortInfo = new ArrayList<SortInfo>(sortCount);
            for (int i = 0; i < sortCount; i++) {
                ColumnSortInfo info = sortList.get(i);
                sortInfo.add(new SortInfo(
                        info.getColumn().getDataStoreName(), 
                        info.isAscending()));
            }
        }
        
        return sortInfo;
    }
    
    protected abstract void onRangeChange(int start, int length);

    protected void setRangeData(Integer totalCount, int start, int length, 
            List<T> dataList) {
        
        //final int dataCount = dataList.size();
        tableData.setList(dataList);
        rowsImpl.doSetRange(start, length);

        if (totalCount != null) {
            rowsImpl.setRowCount(totalCount);
        
        } else if (!exactRowCount) {
            final int count;
            if (dataList.size() >= length) {
                count = start + dataList.size() + 1;
            } else {
                count = start + dataList.size();
            }
            
            rowsImpl.setRowCount(count, false);
        }
    }


    private class HasRowsImpl implements HasRows {
        
        private final HandlerManager    handlerManager;
        private Range                   range;
        private int                     count;
        
        
        public HasRowsImpl() {
            handlerManager = new HandlerManager(this);
            range = new Range(0, DEFAULT_PAGE_SIZE);
        }
        
        @Override
        public void fireEvent(GwtEvent<?> event) {
            handlerManager.fireEvent(event);
        }
        
        @Override
        public void setVisibleRange(Range range) {
            setVisibleRange(range.getStart(), range.getLength());
        }
        
        @Override
        public void setVisibleRange(int start, int length) {
            if (count > 0) {
                onRangeChange(start, length);
            }
        }
        
        @Override
        public void setRowCount(int count) {
            setRowCount(count, true);
        }
        
        @Override
        public void setRowCount(int count, boolean isExact) {
            int old = this.count;
            this.count = count;
            
            if (old != count) {
                RowCountChangeEvent.fire(this, count, isExact);
            }
        }
        
        public void doSetRange(int start, int length) {
            Range old = this.range;
            range = new Range(start, length);
            
            if (!old.equals(range)) {
                RangeChangeEvent.fire(this, range);
            }
        }
        
        @Override
        public boolean isRowCountExact() {
            return true;
        }
        
        @Override
        public Range getVisibleRange() {
            return range;
        }
        
        @Override
        public int getRowCount() {
            return count;
        }
        
        @Override
        public HandlerRegistration addRowCountChangeHandler(
                RowCountChangeEvent.Handler handler) {
            
            return handlerManager.addHandler(RowCountChangeEvent.getType(), 
                    handler);
        }
        
        @Override
        public HandlerRegistration addRangeChangeHandler(
                RangeChangeEvent.Handler handler) {
            
            return handlerManager.addHandler(RangeChangeEvent.getType(), 
                    handler);
        }
    }
    
}
