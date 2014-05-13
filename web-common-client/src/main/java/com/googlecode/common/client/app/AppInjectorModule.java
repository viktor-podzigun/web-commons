
package com.googlecode.common.client.app;

import javax.inject.Singleton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;


/**
 * {@link AppInjector} configuration.
 */
public class AppInjectorModule extends AbstractGinModule {
    
    @Override
    protected void configure() {
        if (GWT.isProdMode()) {
            bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        } else {
            bind(EventBus.class).to(TestingEventBus.class).in(Singleton.class);
        }
    }

}
