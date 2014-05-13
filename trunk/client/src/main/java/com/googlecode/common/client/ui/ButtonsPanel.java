
package com.googlecode.common.client.ui;

import java.util.Collection;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Widget;


/**
 * Represents generic buttons panel.
 */
public final class ButtonsPanel extends Composite {
    
    /** This value indicates that buttons should be left-justified. */
    public static final HorizontalAlignmentConstant LEFT = 
        HasHorizontalAlignment.ALIGN_LEFT;

    /** This value indicates that buttons should be centered. */
    public static final HorizontalAlignmentConstant CENTER = 
        HasHorizontalAlignment.ALIGN_CENTER;

    /** This value indicates that buttons should be right-justified. */
    public static final HorizontalAlignmentConstant RIGHT = 
        HasHorizontalAlignment.ALIGN_RIGHT;

    private final FlowPanel         container = new FlowPanel();
    private ActionProvider          actionProvider;
    private Collection<String>      actionsList;
    

    public ButtonsPanel() {
        this((ButtonType[])null);
    }
    
    public ButtonsPanel(ButtonType... buttonTypes) {
        this(CENTER, buttonTypes);
    }
    
    public ButtonsPanel(HorizontalAlignmentConstant align, 
            ButtonType... buttonTypes) {

        setGroup(false);
        initWidget(container);
        
        if (buttonTypes != null) {
            for (ButtonType type : buttonTypes) {
                addButton(type);
            }
        }
    }
    
    public void setGroup(boolean group) {
        if (group) {
            container.setStyleName("btn-group");
        } else {
            container.setStyleName("btn-toolbar");
        }
    }

    public AbstractButton addButton(ButtonType type) {
        return insertButton(-1, type);
    }

    public AbstractButton addButton(AbstractButton btn) {
        return insertButton(-1, btn);
    }

    public AbstractButton insertButton(int index, ButtonType type) {
        ImageButton b = new ImageButton();
        b.setText(type.getText());
        b.setCommand(type.getCommand());
        b.setImage(type.getImage());
        b.setDisabledImage(type.getDisabledImage());
        
        return insertButtonImpl(index, b);
    }

    public AbstractButton insertButton(int index, AbstractButton btn) {
        return insertButtonImpl(index, btn);
    }

    private AbstractButton insertButtonImpl(int index, final AbstractButton b) {
        if (index == -1) {
            index = container.getWidgetCount();
        }
        
        if (b instanceof HasClickHandlers) {
            ((HasClickHandlers)b).addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (actionProvider != null) {
                        actionProvider.actionPerformed(b.getCommand());
                    }
                }
            });
        }
        
        container.insert(b, index);
        updateButtonAction(b);
        return b;
    }
    
    private void updateButtonAction(Widget w) {
        if (w instanceof AbstractButton) {
            AbstractButton b = (AbstractButton)w;
            b.setEnabled(actionsList != null 
                    && actionsList.contains(b.getCommand()));
        }
    }
    
    public void setActionProvider(ActionProvider actionProvider) {
        this.actionProvider = actionProvider;
        this.actionsList = null;
        if (actionProvider != null) {
            actionsList = actionProvider.getActionCommands();
        }
        
        final int count = container.getWidgetCount();
        for (int i = 0; i < count; i++)  {
            updateButtonAction(container.getWidget(i));
        }
    }
    
    public void setShowText(boolean showText) {
        final int count = container.getWidgetCount();
        for (int i = 0; i < count; i++)  {
            Widget c = container.getWidget(i);
            if (c instanceof AbstractButton) {
                AbstractButton b = (AbstractButton)c;
                String text = b.getText();
                
                b.setText(showText ? String.valueOf(text) : null);
                b.setTitle(showText ? null : text);
            }
        }
    }
    
}
