package com.hon.eventbus;

/**
 * Created by Frank_Hon on 5/5/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class EventBus {

    static volatile EventBus defaultInstance;

    private EventBus() {

    }

    public static EventBus getDefault() {
        EventBus instance = defaultInstance;
        if (instance == null) {
            synchronized (EventBus.class) {
                instance = defaultInstance;
                if (instance == null) {
                    instance = defaultInstance = new EventBus();
                }
            }
        }

        return instance;
    }

    public void register(Object subscriber){

    }

    // unregister the given subscriber from all event classes
    public synchronized void unregister(Object subscriber){

    }

    // posts the given event to the event bus
    public void post(Object event){

    }

}
