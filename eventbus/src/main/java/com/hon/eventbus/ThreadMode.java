package com.hon.eventbus;

/**
 * Each subscriber method has a thread mode,which determines in which thread the method is to be called by EventBus.
 * EventBus takes care of threading independently from the posting thread
 *
 * Created by Frank_Hon on 5/6/2019.
 * E-mail: v-shhong@microsoft.com
 */
public enum ThreadMode {
    /**
     * Subscribers will be called directly in the same thread,which is posting the event. This is the default.
     * Event handlers using this mode must return quickly to avoid blocking the posting thread, which may be the main thread.
     */
    POSTING,

    MAIN,

    MAIN_ORDERED,

    BACKGROUND,

    ASYNC
}
