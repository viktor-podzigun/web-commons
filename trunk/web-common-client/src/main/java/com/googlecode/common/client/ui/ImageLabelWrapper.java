
package com.googlecode.common.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;


/**
 * Common implementation for image with label.
 */
final class ImageLabelWrapper {

    private final Element   element;
    
    private Image           image;
    private Element         label;
    private Element         icon;  // Glyph Icon
    
    private ImageResource   imageRes;
    private ImageResource   disabledImageRes;
    private String          text;
    private boolean         alignText;
    
    
    public ImageLabelWrapper(Element element) {
        this(element, null, null);
    }
    
    public ImageLabelWrapper(Element element, String text) {
        this(element, null, text);
    }
    
    public ImageLabelWrapper(Element element, ImageResource resource, 
            String text) {
        
        this(element, resource, text, true);
    }
        
    public ImageLabelWrapper(Element element, ImageResource resource, 
            String text, boolean alignText) {
        
        this.element   = element;
        this.imageRes  = resource;
        this.text      = text;
        this.alignText = alignText;
        
        updateImage(resource);
        updateText(text);
    }
    
    public Element getElement() {
        return element;
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
        
        updateText(text);
    }
    
    public void setGlyphIcon(String iconName) {
        updateIcon(iconName);
    }
    
    private void updateIcon(String iconName) {
        if (image != null) {
            DOM.removeChild(getElement(), image.getElement());
            image = null;
        }
        
        
        if (iconName != null) {
            if (icon == null) {
                icon = DOM.createElement("i");
                
                if (label != null) {
                    // insert created icon before label
                    DOM.insertBefore(getElement(), icon, label);
                } else {
                    // append created icon to the end
                    DOM.insertChild(getElement(), icon, Integer.MAX_VALUE);
                }
            }
            
            // set icon attributes
            icon.setClassName(iconName);
            
        } else {
            if (icon != null) {
                DOM.removeChild(getElement(), icon);
                icon = null;
            }
        }
    }
    
    private void updateText(String text) {
        if (text != null) {
            if (label == null) {
                String style = "padding-left:3px;";
                if (alignText) {
                    style += " vertical-align:middle;";
                }
                
                // create separate span element for label
                label = DOM.createSpan();
                label.setAttribute("style", style);
                
                // append it to the end
                DOM.insertChild(getElement(), label, Integer.MAX_VALUE);
            }
            
            // set text to our span element
            label.setInnerText(text);
        
        } else {
            if (label != null) {
                DOM.removeChild(getElement(), label);
                label = null;
            }
        }
    }

    private void updateImage(ImageResource resource) {
        if (icon != null) {
            DOM.removeChild(getElement(), icon);
            icon = null;
        }
        
        if (resource != null) {
            String definedStyles;
            if (image == null) {
                image = new Image(resource);
                
                definedStyles = image.getElement().getAttribute("style");
                
                if (label != null) {
                    // insert created image before label
                    DOM.insertBefore(getElement(), image.getElement(), label);
                } else {
                    // append created image to the end
                    DOM.insertChild(getElement(), image.getElement(), 
                            Integer.MAX_VALUE);
                }
            } else {
                definedStyles = new Image(resource).getElement()
                        .getAttribute("style");
                
                // reset current image element
                image.setResource(resource);
            }
            
            // set image attributes
            image.getElement().setAttribute("style", definedStyles 
                    + "; vertical-align:middle;");
        } else {
            if (image != null) {
                DOM.removeChild(getElement(), image.getElement());
                image = null;
            }
        }
    }

    public void setEnabled(boolean enabled) {
        if (disabledImageRes != null) {
            updateImage(enabled ? imageRes : disabledImageRes);
        }
    }

    public void setImage(ImageResource resource) {
        if (this.imageRes == resource) {
            return;
        }
        
        this.imageRes = resource;
        updateImage(resource);
    }

    public void setDisabledImage(boolean enabled, ImageResource resource) {
        this.disabledImageRes = resource;
        if (!enabled) {
            updateImage(resource != null ? resource : imageRes);
        }
    }

}
