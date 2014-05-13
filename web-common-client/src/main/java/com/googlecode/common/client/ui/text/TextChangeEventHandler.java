
package com.googlecode.common.client.ui.text;

import com.google.gwt.event.shared.EventHandler;


/**
 * Text change event handler for such events as KeyUP or PASTE.
 */
public interface TextChangeEventHandler extends EventHandler {

    public void onTextChange(TextChangeEvent event);

}
