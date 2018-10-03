package com.penseapp.acaocontabilidade.news.interactor;

/**
 * Created by unity on 21/11/16.
 */

public interface NewsInteractor {
    void subscribeForNewsUpdates();
    void unsubscribeForNewsUpdates();
    void subscribeForSingleNewsItemUpdates(String newsId);
    void unsubscribeForSingleNewsItemUpdates(String newsId);
}
