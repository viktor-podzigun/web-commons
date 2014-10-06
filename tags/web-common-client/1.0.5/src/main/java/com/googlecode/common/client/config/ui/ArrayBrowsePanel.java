
package com.googlecode.common.client.config.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.googlecode.common.client.config.schema.ArrayNode;
import com.googlecode.common.client.util.StringHelpers;


public final class ArrayBrowsePanel extends Composite {

    
    public ArrayBrowsePanel(ArrayTreeNode treeNode) {
        initWidget(createPanel(treeNode.getArrayNode()));
    }
    
    private HTMLPanel createPanel(ArrayNode<?> arrNode) {
        StringBuilder html = new StringBuilder(512);
        html.append("<div class=\"row-fluid\"><p>").append(
                getDescription(arrNode)).append("</p>");
        html.append("<hr /></div>");
        html.append("<div class=\"row-fluid\"><dl class=\"span12\">");
        
        addArrayInfo(html, arrNode);
        
        html.append("</dl></div>");
        return new HTMLPanel(html.toString());
    }
    
    public static String getDescription(ArrayNode<?> arr) {
        String desc = arr.getDescription();
        if (desc != null) {
            return desc;
        }
        
        return StringHelpers.str(ObjectTreeNode.getName(arr));
    }
    
    public static void addArrayInfo(StringBuilder html, ArrayNode<?> arr) {
        html.append("<dt>Array Type</dt><dd>").append(arr.getArrayType())
                .append("</dd>");
        
        int minItems = arr.getMinItems();
        if (minItems != 0) {
            html.append("<dt>Min Items</dt><dd>").append(minItems)
                    .append("</dd>");
        }
        
        int maxItems = arr.getMaxItems();
        if (maxItems != Integer.MAX_VALUE) {
            html.append("<dt>Max Items</dt><dd>").append(maxItems)
                    .append("</dd>");
        }
        
        if (arr.isUniqueItems()) {
            html.append("<dt>Unique Items</dt><dd>true</dd>");
        }
    }
    
}
