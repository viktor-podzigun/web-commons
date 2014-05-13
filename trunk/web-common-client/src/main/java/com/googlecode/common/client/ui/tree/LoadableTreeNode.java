
package com.googlecode.common.client.ui.tree;


/**
 * Loadable tree node content provider.
 */
public interface LoadableTreeNode {

    public void onNodeOpen();
    
    public void onNodeClose();
    
    public void onLoadChildren();

}
