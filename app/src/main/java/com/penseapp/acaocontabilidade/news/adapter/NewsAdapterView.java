package com.penseapp.acaocontabilidade.news.adapter;

import com.penseapp.acaocontabilidade.news.model.News;

/**
 * Created by unity on 14/12/16.
 */

public interface NewsAdapterView {
    void subscribeForNewsUpdates();
    void unsubscribeForNewsUpdates();
    void onNewsAdded(News news);
    void onNewsChanged(News news);
    void onNewsRemoved(String newsId);
}
