package com.hon.eventbus;

import java.lang.reflect.Method;

import androidx.annotation.Nullable;

/**
 * Created by Frank_Hon on 5/6/2019.
 * E-mail: v-shhong@microsoft.com
 *
 * generated subscriber indexes
 */
public class SubscriberMethod {
    final Method method;
    final ThreadMode threadMode;
    final Class<?> eventType;
    final int priority;
    final boolean sticky;
    // used for efficient comparision
    String methodString;

    public SubscriberMethod(Method method, ThreadMode threadMode, Class<?> eventType, int priority, boolean sticky) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.priority = priority;
        this.sticky = sticky;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==this){
            return true;
        }else if(obj instanceof SubscriberMethod){
            checkMethodString();
            SubscriberMethod otherSubscriberMethod= (SubscriberMethod) obj;
            otherSubscriberMethod.checkMethodString();

            return methodString.equals(otherSubscriberMethod.methodString);
        }
        
        return false;
    }

    private synchronized void checkMethodString(){
        if(methodString==null){
            // Method.toString has more overhead,just take relevant parts of the method
            StringBuilder builder=new StringBuilder(64);
            builder.append(method.getDeclaringClass().getName());
            builder.append('#').append(method.getName());
            builder.append('(').append(eventType.getName());
            methodString=builder.toString();
        }
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}
