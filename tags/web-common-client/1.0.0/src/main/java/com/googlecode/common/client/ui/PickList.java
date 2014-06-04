
package com.googlecode.common.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;


/**
 * Panel with two listboxes and four buttons that allow visually edit list 
 * content.
 */
public class PickList<T> extends Composite {

    private static final Binder binder = GWT.create(Binder.class);
    @SuppressWarnings("rawtypes")
    interface Binder extends UiBinder<Widget, PickList> {
    }

    @UiField ImageLabel                 sourceLabel;
    @UiField ImageLabel                 destLabel;
    
    @UiField(provided=true) CellList<T> sourceList;
    @UiField(provided=true) CellList<T> destList;

    @UiField Button                     addButton;
    @UiField Button                     addAllButton;
    @UiField Button                     removeButton;
    @UiField Button                     removeAllButton;

    private final SortedListModel<T>    sourceListModel;
    private final MultiSelectionModel<T> sourceSelModel;
    private final SortedListModel<T>    destListModel;
    private final MultiSelectionModel<T> destSelModel;

    private boolean                     addEnabled      = true;
    private boolean                     removeEnabled   = true;
    
    private Command                     changeCommand;
    
    
    public PickList(Cell<T> cellRenderer) {
        this(cellRenderer, null, null);
    }

    public PickList(Cell<T> cellRenderer, List<T> srcList) {
        this(cellRenderer, srcList, null);
    }

    public PickList(Cell<T> cellRenderer, List<T> srcList, List<T> dstList) {
        if (cellRenderer == null) {
            throw new NullPointerException("cellRenderer");
        }
        
        sourceSelModel = new MultiSelectionModel<T>();
        sourceSelModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                onSrcListSelectionChanged();
            }
        });
        sourceList = new CellList<T>(cellRenderer);
        sourceList.setSelectionModel(sourceSelModel);
        sourceListModel = new SortedListModel<T>();
        sourceListModel.addDataDisplay(sourceList);

        destSelModel = new MultiSelectionModel<T>();
        destSelModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                onDstListSelectionChanged();
            }
        });
        destList = new CellList<T>(cellRenderer);
        destList.setSelectionModel(destSelModel);
        destListModel = new SortedListModel<T>();
        destListModel.addDataDisplay(destList);
        
        initWidget(binder.createAndBindUi(this));

        if (dstList != null) {
            setDestinationList(dstList);
        }
        if (srcList != null) {
            setSourceList(srcList);
        }
    }

    @UiHandler("addButton")
    void onAddButtonClick(ClickEvent event) {
        fireDataChanged(sourceSelModel.getSelectedSet(), 
                sourceListModel, destListModel, true);
    }
    
    @UiHandler("addAllButton")
    void onAddAllButtonClick(ClickEvent event) {
        fireDataChanged(sourceListModel.getList(), 
                sourceListModel, destListModel, true);
    }
    
    @UiHandler("removeButton")
    void onRemoveButtonClick(ClickEvent event) {
        fireDataChanged(getDstListValues(false), 
                destListModel, sourceListModel, false);
    }
    
    @UiHandler("removeAllButton")
    void onRemoveAllButtonClick(ClickEvent event) {
        fireDataChanged(getDstListValues(true), 
                destListModel, sourceListModel, false);
    }

    public String getSourceTitle() {
        return sourceLabel.getText();
    }

    public void setSourceTitle(String newValue) {
        sourceLabel.setText(newValue);
    }

    public String getDestinationTitle() {
        return destLabel.getText();
    }

    public void setDestinationTitle(String newValue) {
        destLabel.setText(newValue);
    }

    public List<T> getSourceList() {
        return new ArrayList<T>(sourceListModel.getList());
    }

    public void setSourceList(List<T> srcList) {
        sourceListModel.clear();
        sourceSelModel.clear();
        if (srcList != null) {
            sourceListModel.addAll(srcList);
        }
        
        mergeSrcList();
        onSourceListChanged();
    }

    public List<T> getDestinationList() {
        return new ArrayList<T>(destListModel.getList());
    }
    
    public void setDestinationList(List<T> dstList) {
        destListModel.clear();
        destSelModel.clear();
        if (dstList != null) {
            destListModel.addAll(dstList);
        }
        
        if (mergeSrcList()) {
            sourceSelModel.clear();
            onSourceListChanged();
        }
        
        onDestinationListChanged();
    }

    public void setLists(List<T> srcList, List<T> dstList) {
        setDestinationList(dstList);
        setSourceList(srcList);
    }
    
    public void setChangeCommand(Command cmd) {
        changeCommand = cmd;
    }

    public void setAddEnabled(boolean enabled) {
        addEnabled = enabled;
        
        if (enabled) {
            onSourceListChanged();
            onSrcListSelectionChanged();
        } else {
            addButton.setEnabled(false);
            addAllButton.setEnabled(false);
        }
    }

    public boolean isAddEnabled() {
        return addEnabled;
    }
    
    public void setRemoveEnabled(boolean enabled) {
        removeEnabled = enabled;
        
        if (enabled) {
            onDestinationListChanged();
            onDstListSelectionChanged();
        } else {
            removeButton.setEnabled(false);
            removeAllButton.setEnabled(false);
        }
    }

    public boolean isRemoveEnabled() {
        return removeEnabled;
    }

    private boolean mergeSrcList() {
        return sourceListModel.removeAll(destListModel.getList());
    }
    
    private void onSrcListSelectionChanged() {
        if (!addEnabled) {
            return;
        }
        
        if (sourceSelModel.getSelectedSet().isEmpty()) {
            addButton.setEnabled(false);
        } else {
            addButton.setEnabled(true);
        }
    }

    private void onDstListSelectionChanged() {
        if (!removeEnabled) {
            return;
        }

        List<T> data = getDstListValues(false);
        if (data.isEmpty()) {
            removeButton.setEnabled(false);
        } else {
            removeButton.setEnabled(true);
        }
    }

    protected void onChangeValues(Collection<T> items, List<T> srcList, 
            List<T> dstList, boolean isAddOperation) {
        
        setLists(srcList, dstList);
    }

    private void notifyListeners() {
        if (changeCommand != null) {
            changeCommand.execute();
        }
    }
    
    private void onSourceListChanged() {
        if (!addEnabled) {
            return;
        }

        if (sourceListModel.getList().size() > 0) {
            addAllButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
            addAllButton.setEnabled(false);
        }
    }

    private void onDestinationListChanged() {
        if (!removeEnabled) {
            return;
        }

        List<T> data = getDstListValues(true);
        if (!data.isEmpty()) {
            removeAllButton.setEnabled(true);
        } else {
            removeButton.setEnabled(false);
            removeAllButton.setEnabled(false);
        }
    }

    private List<T> getDstListValues(boolean all) {
        List<T> data = new ArrayList<T>();
        if (all) {
            data.addAll(destListModel.getList());
        } else {
            data.addAll(destSelModel.getSelectedSet());
        }
        
        Iterator<T> iter = data.iterator();
        while (iter.hasNext()) {
            T o = iter.next();
            if (o instanceof Item) {
                if (!((Item)o).isMovable()) {
                    iter.remove();
                }
            }
        }
        
        return data;
    }
    
    private void fireDataChanged(Collection<T> objects, 
            SortedListModel<T> srcListModel, SortedListModel<T> dstListModel, 
            boolean isAddOperation) {
        
        if (objects == null) {
            return;
        }
        
        ArrayList<T> dstList = new ArrayList<T>(objects);
        dstList.addAll(dstListModel.getList());
        
        ArrayList<T> list = new ArrayList<T>(srcListModel.getList());
        list.removeAll(objects);

        if (isAddOperation) {
            onChangeValues(objects, list, dstList, true);
        } else {
            onChangeValues(objects, dstList, list, false);
        }
        
        notifyListeners();
    }

    
    private static class SortedListModel<T> extends ListDataProvider<T> {

        public void addAll(List<T> list) {
            if (list == null) {
                return;
            }
            
            List<T> data = getList();
            data.addAll(list);
            
            Collections.sort(data, new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    if (o1 instanceof Item) {
                        Item i1 = (Item)o1;
                        Item i2 = (Item)o2;
                        if (i1.isMovable() != i2.isMovable()) {
                            return (i1.isMovable() ? 1 : -1);
                        }
                    }
                    
                    return o1.toString().compareTo(o2.toString());
                }
            });
            
            refresh();
        }

        public void clear() {
            setList(new ArrayList<T>());
        }

        public boolean removeAll(List<T> list) {
            List<T> model = new ArrayList<T>(getList());
            boolean res = model.removeAll(list);
            setList(model);
            return res;
        }
    }
    
    
    public static interface Item {
        
        public boolean isMovable();
    
    }
    
}
