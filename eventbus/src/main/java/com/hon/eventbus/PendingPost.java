package com.hon.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank_Hon on 5/10/2019.
 * E-mail: v-shhong@microsoft.com
 */
final class PendingPost {
    private final static List<PendingPost> pendingPostPool=new ArrayList<>();

    Object event;
    Subscription subscription;
    PendingPost next;

    private PendingPost(Object event,Subscription subscription){
        this.event=event;
        this.subscription=subscription;
    }

    static PendingPost obtainPendingPost(Subscription subscription,Object event){
        synchronized (pendingPostPool){
            int size=pendingPostPool.size();
            if(size>0){
                PendingPost pendingPost=pendingPostPool.remove(size-1);
                pendingPost.event=event;
                pendingPost.subscription=subscription;
                pendingPost.next=null;
                return pendingPost;
            }
        }

        return new PendingPost(event,subscription);
    }

    static void releasePendingPost(PendingPost pendingPost){
        pendingPost.event=null;
        pendingPost.subscription=null;
        pendingPost.next=null;
        synchronized (pendingPostPool){
            if(pendingPostPool.size()<10000){
                pendingPostPool.add(pendingPost);
            }
        }
    }
}
