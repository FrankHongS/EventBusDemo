package com.hon.eventbus;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Frank_Hon on 5/5/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class HandlerPoster extends Handler implements Poster{

    private final int maxMillisInsideHandleMessage;
    private final EventBus eventBus;

    protected HandlerPoster(EventBus eventBus, Looper looper,int maxMillisInsideHandleMessage){
        super(looper);
        this.eventBus=eventBus;
        this.maxMillisInsideHandleMessage=maxMillisInsideHandleMessage;
    }

    @Override
    public void enqueue(Subscription subscription, Object event) {

    }
}
