package com.penseapp.acaocontabilidade.news.presenter;

import com.penseapp.acaocontabilidade.news.interactor.NewsNotificationsInteractor;
import com.penseapp.acaocontabilidade.news.interactor.NewsNotificationsInteractorImpl;
import com.penseapp.acaocontabilidade.news.view.NewsView;

/**
 * Created by unity on 23/01/17.
 */

public class NewsNotificationsPresenterImpl implements NewsNotificationsPresenter {

    private final NewsNotificationsInteractor newsNotificationsInteractor;
    private final NewsView newsView;

    public NewsNotificationsPresenterImpl(NewsView newsView) {
        this.newsNotificationsInteractor = new NewsNotificationsInteractorImpl(this);
        this.newsView = newsView;
    }

    @Override
    public void subscribeToNewsNotifications() {
        newsNotificationsInteractor.subscribeToNewsNotifications();
    }

    @Override
    public void unsubscribeFromNewsNotifications() {
        newsNotificationsInteractor.unsubscribeFromNewsNotifications();
    }

    @Override
    public void onReceiveNewsNotificationsSubscriptionStatus(boolean isSubscribed) {
        newsView.onReceiveNewsNotificationsSubscriptionStatus(isSubscribed);
    }
}
