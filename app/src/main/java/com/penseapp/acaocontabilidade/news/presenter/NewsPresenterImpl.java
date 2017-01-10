package com.penseapp.acaocontabilidade.news.presenter;

import com.penseapp.acaocontabilidade.news.adapter.NewsAdapterView;
import com.penseapp.acaocontabilidade.news.interactor.NewsInteractor;
import com.penseapp.acaocontabilidade.news.interactor.NewsInteractorImpl;
import com.penseapp.acaocontabilidade.news.model.News;
import com.penseapp.acaocontabilidade.news.view.NewsItemView;

/**
 * Created by unity on 21/11/16.
 */

public class NewsPresenterImpl implements NewsPresenter {

    private final NewsAdapterView newsAdapterView;
    private final NewsInteractor newsInteractor;
    private final NewsItemView newsItemView;


    public NewsPresenterImpl(NewsItemView newsItemView) {
        this.newsAdapterView = null;
        this.newsInteractor = new NewsInteractorImpl(this);
        this.newsItemView = newsItemView;
    }

    public NewsPresenterImpl(NewsAdapterView newsAdapterView) {
        this.newsAdapterView = newsAdapterView;
        this.newsInteractor = new NewsInteractorImpl(this);
        this.newsItemView = null;
    }

    @Override
    public void subscribeForNewsUpdates() {
        newsInteractor.subscribeForNewsUpdates();
    }

    @Override
    public void unsubscribeForNewsUpdates() {
        newsInteractor.unsubscribeForNewsUpdates();
    }

    @Override
    public void onNewsAdded(News news) {
        newsAdapterView.onNewsAdded(news);
//        contactsView.onNewsAdded(contact);
    }

    @Override
    public void onNewsChanged(News news) {
        newsAdapterView.onNewsChanged(news);
//        contactsView.onNewsChanged(contact);
    }

    @Override
    public void onNewsRemoved(String newsId) {
        newsAdapterView.onNewsRemoved(newsId);
//        contactsView.onNewsRemoved(contactId);
    }

    @Override
    public void subscribeForSingleNewsItemUpdates(String newsId) {
        newsInteractor.subscribeForSingleNewsItemUpdates(newsId);
    }

    @Override
    public void unsubscribeForSingleNewsItemUpdates(String newsId) {
        newsInteractor.unsubscribeForSingleNewsItemUpdates(newsId);
    }

    @Override
    public void onNewsItemChanged(News newsItem) {
        newsItemView.onNewsItemChanged(newsItem);
    }
}
