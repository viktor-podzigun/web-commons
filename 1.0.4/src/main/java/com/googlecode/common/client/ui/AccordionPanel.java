
package com.googlecode.common.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Represents accordion panel.
 */
public final class AccordionPanel extends Composite {

    private final FlowPanel     accordion;
    
    
    public AccordionPanel() {
        accordion = new FlowPanel();
        accordion.setStyleName("accordion");
        accordion.getElement().setId(DOM.createUniqueId());
        
        initWidget(accordion);
    }
    
    public void add(String title, Widget body) {
        accordion.add(new Group(title, body));
    }

    public void add(ImageResource image, String title, Widget body) {
        accordion.add(new Group(image, title, body));
    }
    
    public void clear() {
        accordion.clear();
    }


    private class Group extends Composite {

        public Group(String title, Widget widget) {
            this(null, title, widget);
        }
        
        public Group(ImageResource image, String title, Widget widget) {
            String itemId = DOM.createUniqueId();
            
            FlowPanel item = new FlowPanel();
            item.setStyleName("accordion-group");
            
            item.add(createHeading(itemId, image, title));
            item.add(createBody(itemId, widget));
            initWidget(item);
        }
        
        private Widget createHeading(String itemId, ImageResource image, 
                String title) {
            
            FlowPanel heading = new FlowPanel();
            heading.setStyleName("accordion-heading");
            
            //Anchor a = new Anchor(title, "#" + itemId);
            Anchor a = new Anchor();
            a.setHref("#" + itemId);
            a.setStyleName("accordion-toggle");
            
            Element el = a.getElement();
            el.setAttribute("data-toggle", "collapse");
            el.setAttribute("data-parent", "#" 
                    + accordion.getElement().getId());
            
            new ImageLabelWrapper(el, image, title, false);
            
            heading.add(a);
            return heading;
        }

        private Widget createBody(String itemId, Widget widget) {
            SimplePanel body = new SimplePanel();
            String collapse = (accordion.getWidgetCount() == 0 ? 
                    "collapse in" : "collapse");
            body.setStyleName("accordion-body " + collapse);
            body.getElement().setId(itemId);
            
            SimplePanel inner = new SimplePanel();
            inner.setStyleName("accordion-inner");
            inner.setWidget(widget);
            
            body.setWidget(inner);
            return body;
        }
    }

}
