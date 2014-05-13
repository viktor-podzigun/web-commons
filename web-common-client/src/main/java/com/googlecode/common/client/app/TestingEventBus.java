
package com.googlecode.common.client.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.binder.GenericEvent;
import com.google.web.bindery.event.shared.binder.impl.GenericEventType;
import com.google.web.bindery.event.shared.testing.CountingEventBus;


/**
 * Extends functionality of {@link CountingEventBus}. Handy for tests.
 */
public final class TestingEventBus extends EventBus {

    private static final Map<Type<?>, Class<?>> EVENTS = 
        new HashMap<Event.Type<?>, Class<?>>();
    
    private final Map<Type<?>, List<Object>>    handlers = 
        new HashMap<Event.Type<?>, List<Object>>();
    
    private final EventBus  wrapped;

    
    public TestingEventBus() {
        this(new SimpleEventBus());
    }

    public TestingEventBus(EventBus wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public <H> HandlerRegistration addHandler(Type<H> type, H handler) {
        increment(type, handler);
        return makeReg(type, wrapped.addHandler(type, handler), handler);
    }

    @Override
    public <H> HandlerRegistration addHandlerToSource(Type<H> type, 
            Object source, H handler) {
        
        increment(type, handler);
        return makeReg(type, wrapped.addHandlerToSource(type, source, handler), 
                handler);
    }

    @Override
    public void fireEvent(Event<?> event) {
        wrapped.fireEvent(event);
    }

    @Override
    public void fireEventFromSource(Event<?> event, Object source) {
        wrapped.fireEventFromSource(event, source);
    }

    public static void trace(EventBus eventBus) {
        if (!(eventBus instanceof TestingEventBus)) {
            GWT.log("WARNING: eventBus is not TestingEventBus");
            return;
        }
        
        TestingEventBus bus = (TestingEventBus)eventBus;
        
        StringBuilder sb = new StringBuilder();
        int allCount = 0;
        for (Map.Entry<Type<?>, List<Object>> entry : bus.handlers.entrySet()) {
            List<?> list = entry.getValue();
            if (list == null || list.isEmpty()) {
                continue;
            }
            
            int count = list.size();
            allCount += count;
            
            Object event = getEvent(entry.getKey());
            sb.append(count).append(" - ").append(event).append("{\n");
            
            for (Object o : list) {
                sb.append('\t').append(o).append('\n');
            }
            
            sb.append("}\n");
        }
        
        GWT.log("EVENT_BUS found " + allCount + " events handlers:\n" 
                + sb.toString());
    }
    
    public static <T extends GenericEvent> void regEvent(Class<T> eventClass) {
        EVENTS.put(GenericEventType.getTypeOf(eventClass), eventClass);
    }

    private static <H> Object getEvent(Type<H> type) {
        Class<?> clazz = EVENTS.get(type);
        return (clazz != null ? clazz.getName() : type);
    }
    
    private <H> void increment(Type<H> type, H handler) {
        List<Object> list = handlers.get(type);
        if (list == null) {
            list = new ArrayList<Object>();
            handlers.put(type, list);
        }
        
        if (!list.contains(handler)) {
            list.add(handler);
            
            GWT.log("EVENT_BUS handlers(" + list.size() + ") ADDED " 
                    + getEvent(type) + ":\n" + handler);
        }
    }

    private <H> void decrement(Type<H> type, H handler) {
        List<Object> list = handlers.get(type);
        if (list != null && list.remove(handler)) {
            GWT.log("EVENT_BUS handlers(" + list.size() + ") REMOVED " 
                    + getEvent(type) + ":\n" + handler);
        }
    }

    private <H> HandlerRegistration makeReg(final Type<H> type,
            final HandlerRegistration superReg, final H handler) {
        
        return new HandlerRegistration() {
            @Override
            public void removeHandler() {
                decrement(type, handler);
                superReg.removeHandler();
            }
        };
    }

}
