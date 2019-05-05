package com.hon.eventbus;

import java.util.Objects;

/**
 * Created by Frank_Hon on 5/5/2019.
 * E-mail: v-shhong@microsoft.com
 */
final class Subscription {
    final Object subscriber;

    Subscription(Object subscriber){
        this.subscriber=subscriber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(subscriber, that.subscriber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriber);
    }
}
