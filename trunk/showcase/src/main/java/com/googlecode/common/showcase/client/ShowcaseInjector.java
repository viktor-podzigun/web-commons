
package com.googlecode.common.showcase.client;

import com.google.gwt.core.client.GWT;
import com.googlecode.common.client.app.AppInjector;


/**
 * Showcase DI definitions.
 */
public interface ShowcaseInjector extends AppInjector {
    
    static final ShowcaseInjector INSTANCE = GWT.create(ShowcaseInjector.class);
    

}
