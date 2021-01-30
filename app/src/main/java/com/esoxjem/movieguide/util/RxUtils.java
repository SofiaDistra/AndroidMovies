package com.esoxjem.movieguide.util;


import io.reactivex.disposables.Disposable;

/**
 * @author arun
 */
public class RxUtils {

    private RxUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void unsubscribe(Disposable subscription) {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        } // else subscription doesn't exist or already unsubscribed
    }

    public static void unsubscribe(Disposable... subscriptions) {
        for (Disposable subscription : subscriptions) {
            unsubscribe(subscription);
        }
    }
}
