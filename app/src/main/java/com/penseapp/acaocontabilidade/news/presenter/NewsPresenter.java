package com.penseapp.acaocontabilidade.news.presenter;

import com.penseapp.acaocontabilidade.news.model.News;

/**
 * Created by unity on 21/11/16.
 */

public interface NewsPresenter {
    void subscribeForNewsUpdates();
    void unsubscribeForNewsUpdates();
    void onNewsAdded(News news);
    void onNewsChanged(News news);
    void onNewsRemoved(String newsId);
    void subscribeForSingleNewsItemUpdates(String newsId);
    void unsubscribeForSingleNewsItemUpdates(String newsId);
    void onNewsItemChanged(News newsItem);
}
