package com.penseapp.acaocontabilidade.news.view;

/**
 * Created by unity on 23/01/17.
 */

public interface NewsView {
    void subscribeToNewsNotifications();
    void unsubscribeFromNewsNotifications();
    void onReceiveNewsNotificationsSubscriptionStatus(boolean isSubscribed);
}
