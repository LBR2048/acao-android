package com.penseapp.acaocontabilidade.news.presenter;

/**
 * Created by unity on 23/01/17.
 */

public interface NewsNotificationsPresenter {
    void subscribeToNewsNotifications();
    void unsubscribeFromNewsNotifications();
    void isSubscribedToNewsNotifications();
    void onReceiveNewsNotificationsSubscriptionStatus(boolean isSubscribed);
}
