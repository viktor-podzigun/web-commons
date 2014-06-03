
package com.googlecode.common.showcase.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.googlecode.common.client.app.AbstractClientApp;
import com.googlecode.common.client.app.AppMainPanel;
import com.googlecode.common.client.http.RequestService;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.BrowseTreePanel;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.client.ui.tree.BrowseTreeNode;
import com.googlecode.common.protocol.login.LoginRespDTO;
import com.googlecode.common.showcase.client.widgets.ButtonsPanelView;
import com.googlecode.common.showcase.client.widgets.CalendarPanelView;
import com.googlecode.common.showcase.client.widgets.ComboBoxPanelView;
import com.googlecode.common.showcase.client.widgets.DatePickerPanelView;
import com.googlecode.common.showcase.client.widgets.DialogPanelView;
import com.googlecode.common.showcase.client.widgets.DropdownView;
import com.googlecode.common.showcase.client.widgets.ListPanelView;
import com.googlecode.common.showcase.client.widgets.StateButtonPanelView;
import com.googlecode.common.showcase.client.widgets.TabPanelView;
import com.googlecode.common.showcase.client.widgets.TablePanelView;
import com.googlecode.common.showcase.client.widgets.TreePanelView;


/**
 * Defines application entry point <code>onModuleLoad()</code>.
 */
public class Showcase extends AbstractClientApp implements ActionProvider  {

    private static final List<String> CMD_LIST = Arrays.asList(
            ButtonType.REFRESH.getCommand(), 
            ButtonType.ADD.getCommand(), 
            //ButtonType.REMOVE.getCommand(), 
            ButtonType.EDIT.getCommand());
    

    @Override
    protected AppMainPanel createUI(LoginRespDTO loginDto) {
        updateUserInfo(loginDto);
        
        AppMainPanel mainPanel = ShowcaseInjector.INSTANCE.appPanel();
        mainPanel.setCopyright("Web Commons Showcase");
        
        BrowseTreePanel browsePanel = mainPanel.getBrowsePanel().getTreePanel();
        ButtonsPanel bp = browsePanel.getButtonsPanel();
        bp.setActionProvider(this);
        
        BrowseTreeNode root = browsePanel.getRoot();
        
        BrowseTreeNode widgets = new BrowseTreeNode("Widgets");
        root.add(widgets);
        widgets.add(new BrowseTreeNode("HTML", new ScrollPanel(new HTML(
"Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>"
+ "Test HTML Panel gdsfgsdfyguoiugy asdfhsadlkfhsdaklfhsdaklg haigoyfsdigyfsdi gyfsdigysadoidgysdaoigysaoigysgh fhdshf hsdf sdkjhf <br/>", false)), 
                ButtonImages.INSTANCE.info()));
        
        widgets.add(new BrowseTreeNode("Buttons", new ButtonsPanelView(), 
                ButtonImages.INSTANCE.cancel()));
        
        widgets.add(new BrowseTreeNode("Trees", new TreePanelView(), 
                ButtonImages.INSTANCE.folder()));
        
        widgets.add(new BrowseTreeNode("Lists", new ListPanelView(), 
                ButtonImages.INSTANCE.add()));
        
        widgets.add(new BrowseTreeNode("Popups", new DialogPanelView(), 
                ButtonImages.INSTANCE.info()));
        
        widgets.add(new BrowseTreeNode("Tab", new TabPanelView(), 
                ButtonImages.INSTANCE.ok()));
        
        widgets.add(new BrowseTreeNode("Table", new TablePanelView(), 
                ButtonImages.INSTANCE.add()));
        
        widgets.add(new BrowseTreeNode("Dropdown", new DropdownView(), 
                ButtonImages.INSTANCE.add()));
        
        widgets.add(new BrowseTreeNode("StateButton", new StateButtonPanelView(), 
                ButtonImages.INSTANCE.info()));
        
        widgets.add(new BrowseTreeNode("DatePicker", new DatePickerPanelView(), 
                ButtonImages.INSTANCE.ok()));
        
        widgets.add(new BrowseTreeNode("ComboBox", new ComboBoxPanelView(), 
                ButtonImages.INSTANCE.ok()));
        
        widgets.add(new BrowseTreeNode("Calendar", new CalendarPanelView(), 
                ButtonImages.INSTANCE.ok()));
        
        BrowseTreeNode scroll = new BrowseTreeNode("Scrolling");
        root.add(scroll);
        scroll.add(new BrowseTreeNode("Horizontal"))
                .add(new BrowseTreeNode("Toooooooooooooooooooooooooooooooooooooooooooooooloooooooooooooooooooooong teeeeeeeext"));
        BrowseTreeNode vscroll = scroll.add(new BrowseTreeNode("Vertical"));
        for (int i = 0; i < 25; i++) {
            vscroll.add(new BrowseTreeNode("Sub node"));
        }
        
        root.notifyStructureChanged();
        
        // open widgets sub-tree
        CellTree tree = mainPanel.getBrowsePanel().getTreePanel()
                .getBrowseTree();
        TreeNode rootNode = tree.getRootTreeNode();
        for (int i = 0, count = rootNode.getChildCount(); i < count; i++) {
            if (rootNode.getChildValue(i) == widgets) {
                rootNode.setChildOpen(i, true);
                break;
            }
        }
        
        return mainPanel;
    }
    
    @Override
    public Collection<String> getActionCommands() {
        return CMD_LIST;
    }

    @Override
    public void actionPerformed(String actionCommand) {
    }
    
    private void updateUserInfo(LoginRespDTO loginDto) {
        // apply received user permissions
//        UserPermissions.apply(loginDto.safeIsSuperUser(), 
//                null, loginDto.safeGetPermissions());
        
        // set user token
        RequestService.INSTANCE.setUserLogin(loginDto.getLogin());
    }
    
}
