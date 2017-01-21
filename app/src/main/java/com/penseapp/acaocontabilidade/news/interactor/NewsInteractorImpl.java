package com.penseapp.acaocontabilidade.news.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.news.model.News;
import com.penseapp.acaocontabilidade.news.presenter.NewsPresenter;

/**
 * Created by unity on 21/11/16.
 */

public class NewsInteractorImpl implements NewsInteractor {

    private final static String LOG_TAG = NewsInteractorImpl.class.getSimpleName();

    private final NewsPresenter newsPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private ChildEventListener newsChildEventListener;
    private ValueEventListener newsItemEventListener;

    public NewsInteractorImpl(NewsPresenter newsPresenter) {
        this.newsPresenter = newsPresenter;
    }

    @Override
    public void subscribeForNewsUpdates() {
        Log.i(LOG_TAG, "Interactor " + LOG_TAG + " called");

        if (newsChildEventListener == null) {
            newsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i(LOG_TAG, dataSnapshot.toString() + " added");
                    News news = dataSnapshot.getValue(News.class);
                    news.setKey(dataSnapshot.getKey());
                    newsPresenter.onNewsAdded(news);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.i(LOG_TAG, dataSnapshot.toString() + " changed");
                    News news = dataSnapshot.getValue(News.class);
                    news.setKey(dataSnapshot.getKey());
                    newsPresenter.onNewsChanged(news);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.i(LOG_TAG, dataSnapshot.toString() + " removed");
                    String newsId = dataSnapshot.getKey();
                    newsPresenter.onNewsRemoved(newsId);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            // TODO orderByChild só faz diferença ao baixar todos os itens do Firebase
            // Ao adicionar um novo item ele irá para o fim da lista
            mFirebaseHelperInstance.getNewsReference().orderByChild("date").addChildEventListener(newsChildEventListener);
        }
    }

    @Override
    public void unsubscribeForNewsUpdates() {
        if (newsChildEventListener != null) {
            mFirebaseHelperInstance.getNewsReference().removeEventListener(newsChildEventListener);
        }
    }

    @Override
    public void subscribeForSingleNewsItemUpdates(String newsId) {
        if (newsItemEventListener == null) {
            newsItemEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    News changedNewsItem = dataSnapshot.getValue(News.class);
                    newsPresenter.onNewsItemChanged(changedNewsItem);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mFirebaseHelperInstance.getNewsReference().child(newsId).addValueEventListener(newsItemEventListener);
        }
    }

    @Override
    public void unsubscribeForSingleNewsItemUpdates(String newsId) {
        if (newsItemEventListener != null) {
            mFirebaseHelperInstance.getNewsReference().child(newsId).removeEventListener(newsItemEventListener);
        }
    }
}
