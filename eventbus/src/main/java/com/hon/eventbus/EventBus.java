package com.hon.eventbus;

import android.os.Looper;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.Nullable;

/**
 * Created by Frank_Hon on 5/5/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class EventBus {

    static volatile EventBus defaultInstance;

    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionByEventType;
    private final Map<Object,List<Class<?>>> typesBySubscriber;

    private final SubscriberMethodFinder subscriberMethodFinder;

    private final ThreadLocal<PostingThreadState> currentPostingThreadState=new ThreadLocal<PostingThreadState>(){
        @Override
        protected PostingThreadState initialValue() {
            return new PostingThreadState();
        }
    };

    private final Poster mainThreadPoster;

    private EventBus() {
        subscriptionByEventType=new HashMap<>();
        typesBySubscriber=new HashMap<>();
        subscriberMethodFinder=new SubscriberMethodFinder(false);

        MainThreadSupport mainThreadSupport=new MainThreadSupport.AndroidHandlerMainThreadSupport(Looper.getMainLooper());

        mainThreadPoster=mainThreadSupport.createPoster(this);
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

    public void register(Object subscriber) {
        Class<?> subscriberClass=subscriber.getClass();
        List<SubscriberMethod> subscriberMethods=subscriberMethodFinder.findSubscriberMethods(subscriberClass);
        synchronized (this){
            for(SubscriberMethod subscriberMethod:subscriberMethods){
                subscribe(subscriber,subscriberMethod);
            }
        }
    }

    // Must be called in synchronized block
    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        Class<?> eventType=subscriberMethod.eventType;
        Subscription newSubscription=new Subscription(subscriber,subscriberMethod);
        CopyOnWriteArrayList<Subscription> subscriptions=subscriptionByEventType.get(eventType);
        if(subscriptions==null){
            subscriptions=new CopyOnWriteArrayList<>();
            subscriptionByEventType.put(eventType,subscriptions);
        }else {
            if(subscriptions.contains(newSubscription)){
                throw new RuntimeException("Subscriber " + subscriber.getClass() + " already registered to event "
                        + eventType);
            }
        }

        int size=subscriptions.size();
        for(int i=0;i<=size;i++){
            if(i==size||subscriberMethod.priority>subscriptions.get(i).subscriberMethod.priority){
                subscriptions.add(i,newSubscription);
                break;
            }
        }

        List<Class<?>> subscribedEvents=typesBySubscriber.get(subscriber);
        if(subscribedEvents==null){
            subscribedEvents=new ArrayList<>();
            typesBySubscriber.put(subscriber,subscribedEvents);
        }
        subscribedEvents.add(eventType);

    }

    // unregister the given subscriber from all event classes
    public synchronized void unregister(Object subscriber) {
        List<Class<?>> subscribedTypes=typesBySubscriber.get(subscriber);
        if(subscribedTypes!=null){
            for(Class<?> eventType:subscribedTypes){
                unsubscribeByEventType(subscriber,eventType);
            }
            typesBySubscriber.remove(subscriber);
        }else{
            Log.w("EventBus", "Subscriber to unregister was not registered before: " + subscriber.getClass());
        }
    }

    // Only updates subscriptionsByEventType, not typeBySubscriber. Caller must update typesBySubscriber
    private void unsubscribeByEventType(Object subscriber, Class<?> eventType) {
        List<Subscription> subscriptions=subscriptionByEventType.get(eventType);
        if(subscriptions!=null){
            int size=subscriptions.size();
            for(int i=0;i<size;i++){
                Subscription subscription=subscriptions.get(i);
                if(subscription.subscriber==subscriber){
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }

    // posts the given event to the event bus
    public void post(Object event) {
        PostingThreadState postingState=currentPostingThreadState.get();
        List<Object> eventQueue=postingState.eventQueue;
        eventQueue.add(event);

        if(!postingState.isPosting){
            postingState.isPosting=true;
            while (!eventQueue.isEmpty()){
                postSingleEvent(eventQueue.remove(0),postingState);
            }
        }
    }

    private void postSingleEvent(Object event, PostingThreadState postingState) {
        Class<?> eventClass=event.getClass();
        postSingleEventForEventType(event,postingState,eventClass);
    }

    private void postSingleEventForEventType(Object event, PostingThreadState postingState, Class<?> eventClass) {
        CopyOnWriteArrayList<Subscription> subscriptions;
        synchronized (this){
            subscriptions=subscriptionByEventType.get(eventClass);
        }

        if(subscriptions!=null&&!subscriptions.isEmpty()){
            for(Subscription subscription:subscriptions){
                postingState.event=event;
                postingState.subscription=subscription;
                try {
                    postToSubscription(subscription,event);
                }finally {
                    postingState.event=null;
                    postingState.subscription=null;
                }
            }
        }
    }

    private void postToSubscription(Subscription subscription, Object event) {
//        invokeSubscriber(subscription, event);

    }

    private void invokeSubscriber(Subscription subscription, Object event) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber,event);
        } catch (IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    final static class PostingThreadState{
        final List<Object> eventQueue=new ArrayList<>();
        boolean isPosting;
        boolean isMainThread;
        Subscription subscription;
        Object event;
        boolean canceled;
    }

}
