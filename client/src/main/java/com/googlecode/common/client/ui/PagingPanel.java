
package com.googlecode.common.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasRows;


/**
 * Paging panel to use with {@link TablePanel}.
 */
public final class PagingPanel extends AbstractPager {

    private final HTMLPanel     panel;
    private final Page          prevPage;
    private final Page          nextPage;
    
    
    public PagingPanel() {
        panel = new HTMLPanel("ul", "");
        
        SimplePanel widget = new SimplePanel();
        widget.setStyleName("pagination");
        widget.setWidget(panel);
        initWidget(widget);
        
        prevPage = new Page("&laquo;") {
            @Override
            public void onClick(ClickEvent event) {
                event.preventDefault();
                if (!isDisabled()) {
                    previousPage();
                }
            }
        };
        
        nextPage = new Page("&raquo;") {
            @Override
            public void onClick(ClickEvent event) {
                event.preventDefault();
                if (!isDisabled()) {
                    nextPage();
                }
            }
        };
    }
    
    @Override
    protected void setPageStart(int index) {
        super.setPageStart(index);
        updateButtons();
    }

    @Override
    public void setPageSize(int pageSize) {
        super.setPageSize(pageSize);
    }
    
    @Override
    protected void onRangeOrRowCountChanged() {
        panel.clear();
        panel.add(prevPage);
        
        for (int p = 0, maxPage = getPageCount(); p < maxPage; p++) {
            panel.add(new Page(String.valueOf(p + 1), p));
        }
        
        panel.add(nextPage);
        
        updateButtons();
    }
    
    private void updateButtons() {
        HasRows display = getDisplay();

        setPrevPageButtonsDisabled(!hasPreviousPage());
        if (isRangeLimited() || !display.isRowCountExact()) {
            setNextPageButtonsDisabled(!hasNextPage());
        }
        
        final int currPageIndex = getPage();
        for (Widget w : panel) {
            Page page = (Page)w;
            page.setActive(page.pageIndex == currPageIndex);
        }
    }
    
    /**
     * Enable or disable the next page buttons.
     * 
     * @param disabled  true to disable, false to enable
     */
    private void setNextPageButtonsDisabled(boolean disabled) {
        nextPage.setDisabled(disabled);
    }

    /**
     * Enable or disable the previous page buttons.
     * 
     * @param disabled  true to disable, false to enable
     */
    private void setPrevPageButtonsDisabled(boolean disabled) {
        prevPage.setDisabled(disabled);
    }    
    
    
    private class Page extends Composite implements ClickHandler {

        private final Anchor    anchor;
        private final int       pageIndex;
        
        private boolean         disabled;
        private boolean         active = true;
        
        
        public Page(String text) {
            this(text, -1);
        }
        
        public Page(String text, int pageNum) {
            this.pageIndex = pageNum;
            
            anchor = new Anchor();
            anchor.setHref("#");
            anchor.setHTML(text);
            anchor.addClickHandler(this);
            
            HTMLPanel item = new HTMLPanel("li", "");
            item.add(anchor);
        
            initWidget(item);
        }
    
        @Override
        public void onClick(ClickEvent event) {
            event.preventDefault();
            if (pageIndex >= 0 && !isDisabled() && !isActive()) {
                setPageStart(pageIndex * getPageSize());
            }
        }
        
        public boolean isDisabled() {
            return disabled;
        }
        
        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
            if (disabled) {
                addStyleName("disabled");
            } else {
                removeStyleName("disabled");
            }
        }
        
        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
            if (active) {
                addStyleName("active");
            } else {
                removeStyleName("active");
            }
        }
    }
    
}
