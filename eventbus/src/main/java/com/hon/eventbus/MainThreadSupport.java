package com.hon.eventbus;

import android.os.Looper;

/**
 * Created by Frank_Hon on 5/5/2019.
 * E-mail: v-shhong@microsoft.com
 */
public interface MainThreadSupport {

    boolean isMainThread();

    Poster createPoster(EventBus eventBus);

    class AndroidHandlerMainThreadSupport implements MainThreadSupport{

        private final Looper looper;

        public AndroidHandlerMainThreadSupport(Looper looper){
            this.looper=looper;
        }

        @Override
        public boolean isMainThread() {
            return looper==Looper.myLooper();
        }

        @Override
        public Poster createPoster(EventBus eventBus) {
            return new HandlerPoster(eventBus,looper,10);
        }
    }
}
