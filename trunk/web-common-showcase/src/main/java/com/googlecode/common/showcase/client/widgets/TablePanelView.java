
package com.googlecode.common.showcase.client.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.task.AbstractTask;
import com.googlecode.common.client.task.TaskManager;
import com.googlecode.common.client.ui.PagingTablePanel;
import com.googlecode.common.client.ui.TablePanel;
import com.googlecode.common.protocol.SortInfo;
import com.googlecode.common.showcase.client.AbstractPanelView;


/**
 * {@link TablePanel} show-case.
 */
public final class TablePanelView extends AbstractPanelView {

    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, TablePanelView> {
    }
    
    @UiField PagingTableTest    pagingTable;
    
    
    public TablePanelView() {
        initWidget(binder.createAndBindUi(this));
    }
    
    
    private static final class Data {
        
        int     id;
        String  name;
        String  desc;
        
        public Data(int id, String name, String desc) {
            this.id   = id;
            this.name = name;
            this.desc = desc;
        }
    }
    
    
    static class PagingTableTest extends PagingTablePanel<Data> {
        
        public PagingTableTest() {
            initColumns();
        }
        
        public void initColumns() {
            TextColumn<Data> id = new TextColumn<Data>() {
                @Override
                public String getValue(Data dto) {
                    return String.valueOf(dto.id);
                }
            };
            
            final EditTextCell nameCell = new EditTextCell();
            Column<Data, String> name = new Column<Data, String>(nameCell) {
                @Override
                public String getValue(Data dto) {
                    return dto.name;
                }
            };
            name.setSortable(true);
            name.setFieldUpdater(new FieldUpdater<Data, String>() {
                @Override
                public void update(int index, Data object, String value) {
                    if (value != null && !value.equals(object.name) ) {
//                        SettingUpdateDTO dto = new SettingUpdateDTO();
//                        dto.setId(object.safeGetId());
//                        dto.setName(object.getName());
//                        dto.setValue(value);
//                        
//                        nameCell.clearViewData(KEY_PROVIDER.getKey(object));
//                        
//                        TaskManager.INSTANCE.execute(new UpdateSettingTask(object, dto));
                    }
                }
            });
            
            TextColumn<Data> desc = new TextColumn<Data>() {
                @Override
                public String getValue(Data dto) {
                    return dto.desc;
                }
            };
            
            // add the columns
            table.addColumn(id,     "Id");
            table.addColumn(name,   "Name");
            table.getColumnSortList().push(name);
            table.addColumn(desc,   "Description");
            
//            id.setCellStyleNames("span2");
//            name.setCellStyleNames("span2");
//            desc.setCellStyleNames("span8");

            // set the width of the table and the columns
//            table.setWidth("100%", true);
//            table.setColumnWidth(name, 10.0, Unit.PCT);
//            table.setColumnWidth(name, 10.0, Unit.PCT);
//            table.setColumnWidth(desc, 80.0, Unit.PCT);
        }
        
        @Override
        protected void onLoad() {
            super.onLoad();
            
            onRangeChange(0, 10);
        }
        
        @Override
        protected void onRangeChange(int start, int length) {
            TaskManager.INSTANCE.execute(new PagingTableTask(this, 
                    start, length, getSortInfo()));
        }
        
        @Override
        public void setRangeData(Integer totalCount, int start, int length,
                List<Data> dataList) {
            
            super.setRangeData(totalCount, start, length, dataList);
        }
    }
    
    
    private static class PagingTableTask extends AbstractTask {
        
        private final List<Data>        dataList;
        private final PagingTableTest   pagingTable;
        
        private final int               start;
        private final int               length;
        private final List<SortInfo>    sortInfo;
        
        
        public PagingTableTask(PagingTableTest pagingTable, 
                int start, int length, List<SortInfo> sortInfo) {
            
            super("Fetching data...");
            
            this.pagingTable = pagingTable;
            
            this.start    = start;
            this.length   = length;
            this.sortInfo = sortInfo;
        
            // prepare test data for table
            dataList = new ArrayList<Data>();
            for (int i = 1; i <= 100; i++) {
                dataList.add(new Data(i, String.valueOf(i), 
                        String.valueOf(i) + " test row"));
            }
        }
        
        @Override
        protected void runTask() throws Exception {
            // This timer is here to illustrate the asynchronous nature of
            // this data provider. In practice, you would use an 
            // asynchronous RPC call to request data in the specified range
            new Timer() {
                @Override
                public void run() {
                    if (sortInfo != null && !sortInfo.isEmpty()) {
                        final SortInfo info = sortInfo.get(0);
                        
                        // This sorting code is here so the example works. 
                        // In practice, you would sort on the server
                        Collections.sort(dataList, new Comparator<Data>() {
                            @Override
                            public int compare(Data o1, Data o2) {
                                if (o1 == o2) {
                                    return 0;
                                }
    
                                // Compare the name columns.
                                int diff = -1;
                                if (o1 != null) {
                                    diff = (o2 != null ? 
                                            o1.name.compareTo(o2.name) : 1);
                                }
                                
                                return (info.isAscending() ? diff : -diff);
                            }
                        });
                    }
                    
                    pagingTable.setRangeData(dataList.size(), start, length, 
                            dataList.subList(start, start + length));
                    
                    onFinish();
                }
            }.schedule(1000);
        }
    }
    
}
