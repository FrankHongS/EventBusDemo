package com.hon.eventbus;

/**
 * Created by Frank_Hon on 5/5/2019.
 * E-mail: v-shhong@microsoft.com
 */
public interface Poster {

    /**
     * Enqueue an event to be posted for a particular subscription
     *
     * @param subscription Subscription which will receive the event
     * @param event        Event that will be posted to subscribers
     */
    void enqueue(Subscription subscription, Object event);

}
