package com.hon.eventbus;

/**
 * Created by Frank_Hon on 5/10/2019.
 * E-mail: v-shhong@microsoft.com
 */
final class PendingPostQueue {
    private PendingPost head;
    private PendingPost tail;

    synchronized void enqueue(PendingPost pendingPost){
        if(pendingPost==null)
            throw new NullPointerException("null can't be enqueued");

        if(tail!=null){
            tail.next=pendingPost;
            tail=pendingPost;
        }else if(head==null){
            head=tail=pendingPost;
        }else {
            throw new IllegalStateException("head present, but no tail");
        }

        notifyAll();
    }

    synchronized PendingPost poll(){
        PendingPost pendingPost=head;
        if(head!=null){
            head=head.next;
            if(head==null){
                tail=null;
            }
        }

        return pendingPost;
    }

    synchronized PendingPost poll(int maxMillisToWait) throws InterruptedException {
        if(head==null){
            wait(maxMillisToWait);
        }

        return poll();
    }
}
