
package com.googlecode.common.client.app;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.web.bindery.event.shared.EventBus;


/**
 * Common DI definitions.
 */
@GinModules(AppInjectorModule.class)
public interface AppInjector extends Ginjector {
    
    EventBus eventBus();
    
    AppMainPanel appPanel();

}
