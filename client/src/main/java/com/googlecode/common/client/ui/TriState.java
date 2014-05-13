
package com.googlecode.common.client.ui;


/**
 * Tri-state definition.
 * 
 * <p>Got from here: http://www.javaspecialists.eu/archive/Issue145.html
 */
public enum TriState {

    SELECTED {
        @Override
        public TriState next() {
            return DESELECTED;
        }
    },
    
    INDETERMINATE {
        @Override
        public TriState next() {
            return SELECTED;
        }
    },
    
    DESELECTED {
        @Override
        public TriState next() {
            return SELECTED;
        }
    };

    public abstract TriState next();
    
    public static TriState valueOf(boolean isSelected) {
        return (isSelected ? SELECTED : DESELECTED);
    }

}
