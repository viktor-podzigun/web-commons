
package com.googlecode.common.client.config.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.googlecode.common.client.config.schema.ArrayModel;
import com.googlecode.common.client.config.schema.PropertyArrayNode;
import com.googlecode.common.client.config.schema.PropertyNode;
import com.googlecode.common.client.config.schema.PropertyValidationException;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.client.ui.ImageLabel;
import com.googlecode.common.client.ui.panel.LoadablePanel;
import com.googlecode.common.client.ui.panel.MessageBox;
import com.googlecode.common.client.ui.panel.MessageBox.CmdOption;
import com.googlecode.common.client.ui.panel.MessageBox.MsgType;


public final class PropertyArrayBrowsePanel extends LoadablePanel implements 
        ActionProvider {

    private static final String CMD_ADD     = ButtonType.ADD.getCommand();
    private static final String CMD_REMOVE  = ButtonType.REMOVE.getCommand();
    private static final String CMD_EDIT    = ButtonType.EDIT.getCommand();
    
    private static final Binder binder = GWT.create(Binder.class);
    interface Binder extends UiBinder<Widget, PropertyArrayBrowsePanel> {
    }

    @UiField ImageLabel                         description;
    @UiField SimplePanel                        infoPanel;
    
    @UiField(provided=true) ButtonsPanel        buttonsPanel;
    @UiField(provided=true) CellList<Item>      cellList;

    private final ListDataProvider<Item>        dataProvider;
    private final SingleSelectionModel<Item>    listSelModel;

    private final Set<String>       cmdSet = new HashSet<String>();
    private PropertyArrayTreeNode   currNode;
    
    
    public PropertyArrayBrowsePanel() {
        buttonsPanel = new ButtonsPanel(ButtonType.ADD, ButtonType.REMOVE, 
                ButtonType.EDIT);
        buttonsPanel.setGroup(true);
        
        listSelModel = new SingleSelectionModel<Item>();
        listSelModel.addSelectionChangeHandler(
                new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                updateButtons();
            }
        });
        cellList = new CellList<Item>(new ItemCell());
        cellList.setSelectionModel(listSelModel);
        dataProvider = new ListDataProvider<Item>();
        dataProvider.addDataDisplay(cellList);
        
        initWidget(binder.createAndBindUi(this));
    }
    
    private HTMLPanel createInfoPanel(PropertyArrayNode arrNode) {
        StringBuilder html = new StringBuilder("<dl>");
        ArrayBrowsePanel.addArrayInfo(html, arrNode);
        ObjectBrowsePanel.addPropertyInfo(html, arrNode.getItem());
        
        html.append("</dl>");
        return new HTMLPanel(html.toString());
    }
    
    public void setTreeNode(PropertyArrayTreeNode node) {
        this.currNode = node;
        
        setNeedLoad(true);
    }
    
    @Override
    public void onLoadData() {
        ArrayModel model = currNode.getModel();
        PropertyArrayNode arrNode = model.getNode();
        description.setText(arrNode.getDescription());
        infoPanel.setWidget(createInfoPanel(arrNode));
        
        if (currNode.isDefined()) {
            fillList(model);
        } else {
            clearList();
        }
    }
    
    @Override
    public Collection<String> getActionCommands() {
        return cmdSet;
    }

    @Override
    public void actionPerformed(String cmd) {
        if (CMD_ADD.equals(cmd)) {
            onAddAction();
            
        } else if (CMD_REMOVE.equals(cmd)) {
            onRemoveAction();
            
        } else if (CMD_EDIT.equals(cmd)) {
            onEditAction();
        }
    }
    
    private void clearList() {
        // update actions buttons
        cmdSet.clear();
        buttonsPanel.setActionProvider(this);
        
        // clear data
        dataProvider.getList().clear();
        dataProvider.refresh();
    }
    
    private void fillList(ArrayModel model) {
        // update actions buttons
        cmdSet.clear();
        cmdSet.add(CMD_ADD);
        buttonsPanel.setActionProvider(this);
        
        updateList(model);
    }
    
    private void updateList(ArrayModel model) {
        List<Item> list = dataProvider.getList();
        list.clear();
        
        int i = 0;
        for (Object val : model.getValues()) {
            list.add(new Item(i++, val));
        }
        
//        sortList(list);
        dataProvider.refresh();
    }
    
    private Item getItem(int index) {
        for (Item item : dataProvider.getList()) {
            if (item.getIndex() == index) {
                return item;
            }
        }
        
        return null;
    }
    
//    private void sortList(List<Item> list) {
//        Collections.sort(list, new Comparator<Item>() {
//            @Override
//            public int compare(Item item1, Item item2) {
//                Object val1 = item1.getValue();
//                Object val2 = item2.getValue();
//                if (val1 instanceof Comparable) {
//                    @SuppressWarnings("unchecked")
//                    Comparable<Object> comp1 = (Comparable<Object>)val1;
//                    return comp1.compareTo(val2);
//                }
//                
//                return val1.toString().compareTo(val2.toString());
//            }
//        });
//    }
    
    private void updateButtons() {
        Object item = listSelModel.getSelectedObject();
        if (item != null) {
            cmdSet.add(CMD_REMOVE);
            cmdSet.add(CMD_EDIT);
        } else {
            cmdSet.remove(CMD_REMOVE);
            cmdSet.remove(CMD_EDIT);
        }
        
        buttonsPanel.setActionProvider(this);
    }

    private void onAddAction() {
        PropertyNode<?> prop = currNode.getModel().getNode().getItem();
        final MessageBox box = new MessageBox();
        Object def = prop.getDefault();
        box.showInput("Enter new value", def != null ? def.toString() : null, 
                new Command() {
            @Override
            public void execute() {
                if (box.getSelectedOption() == CmdOption.OK) {
                    Object val = validateValue(box.getInputValue());
                    if (val == null) {
                        return;
                    }
                    
                    ArrayModel model = currNode.getModel();
                    int index = model.addValue(val);
                    
                    updateList(model);
                    listSelModel.setSelected(getItem(index), true);
                }
                
                box.hide();
            }
        });
    }

    private void onRemoveAction() {
        Item currItem = listSelModel.getSelectedObject();
        
        // remove from model
        ArrayModel model = currNode.getModel();
        model.remove(currItem.getIndex());
        
        listSelModel.setSelected(currItem, false);

        // remove from view list and re-index
        List<Item> list = dataProvider.getList();
        Item selItem = null;
        int currIndex = currItem.getIndex();
        for (int i = list.size() - 1; i >= 0; i--) {
            Item item = list.get(i);
            if (item.getIndex() == currIndex) {
                Item prev = (i > 0 ? list.get(i - 1) : null);
                Item next = ((i + 1) < list.size() ? list.get(i + 1) : null);
                selItem = (next != null ? next : prev);
                list.remove(i);
            }
            
            item.reIndex(currIndex);
        }
        
        dataProvider.refresh();
        
        if (selItem != null) {
            listSelModel.setSelected(selItem, true);
        }
    }

    private void onEditAction() {
        final Item item = listSelModel.getSelectedObject();
        final MessageBox box = new MessageBox();
        box.showInput("Enter new value", item.getValue().toString(), 
                new Command() {
            @Override
            public void execute() {
                if (box.getSelectedOption() == CmdOption.OK) {
                    Object val = validateValue(box.getInputValue());
                    if (val == null) {
                        return;
                    }
                    
                    ArrayModel model = currNode.getModel();
                    model.setValue(item.getIndex(), val);

                    updateList(model);
                    listSelModel.setSelected(getItem(item.getIndex()), true);
                }
                
                box.hide();
            }
        });
    }
    
    private Object validateValue(String val) {
        try {
            return currNode.getModel().getNode().getItem().parseValue(val);
        
        } catch (PropertyValidationException x) {
            MessageBox.showMessage(x.getMessage(), null, MsgType.ERROR);
            return null;
        }
    }
    
    
    private static class Item {
        
        private int             index;
        private final Object    value;
        
        public Item(int index, Object value) {
            this.index = index;
            this.value = value;
        }
        
        public int getIndex() {
            return index;
        }
        
        public void reIndex(int removeIndex) {
            if (index > removeIndex) {
                index--;
            }
        }
        
        public Object getValue() {
            return value;
        }
    }

    
    private static class ItemCell extends AbstractCell<Item> {
        
        @Override
        public void render(Context context, Item value, SafeHtmlBuilder sb) {
            sb.appendEscaped(value.getValue().toString());
        }
    }

}
