
package com.googlecode.common.client.ui;

import java.util.Collection;


/**
 * Generic action provider.
 */
public interface ActionProvider {
    
    /**
     * Returns list of available command.
     * @return list of available command
     */
    public Collection<String> getActionCommands();

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(String actionCommand);

}
