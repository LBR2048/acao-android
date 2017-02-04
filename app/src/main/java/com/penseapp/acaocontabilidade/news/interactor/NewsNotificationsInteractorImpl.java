package com.penseapp.acaocontabilidade.news.interactor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.news.presenter.NewsNotificationsPresenter;

/**
 * Created by unity on 23/01/17.
 */

public class NewsNotificationsInteractorImpl implements NewsNotificationsInteractor {

    NewsNotificationsPresenter newsNotificationsPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private String userId = mFirebaseHelperInstance.getAuthUserId();
    private DatabaseReference isSubscribedToNewsNotificationsRef =
            mFirebaseHelperInstance.getUsersReference().child(userId).child("isSubscribedToNewsNotifications");
    private ValueEventListener newsNotificationSubscriptionStatusListener;

    public NewsNotificationsInteractorImpl(NewsNotificationsPresenter newsNotificationsPresenter) {
        this.newsNotificationsPresenter = newsNotificationsPresenter;
    }

    @Override
    public void subscribeToNewsNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        isSubscribedToNewsNotificationsRef.setValue(true);
    }

    @Override
    public void unsubscribeFromNewsNotifications() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
        isSubscribedToNewsNotificationsRef.setValue(false);
    }

    @Override
    public void isSubscribedToNewsNotifications() {
//        if (newsNotificationSubscriptionStatusListener == null) {
            newsNotificationSubscriptionStatusListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        newsNotificationsPresenter.onReceiveNewsNotificationsSubscriptionStatus(
                            dataSnapshot.getValue(boolean.class));
                    } else {
                        newsNotificationsPresenter.onReceiveNewsNotificationsSubscriptionStatus(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            // TODO Por que mesmo com single value ele não para de monitorar?
            isSubscribedToNewsNotificationsRef.addListenerForSingleValueEvent(newsNotificationSubscriptionStatusListener);
//        }
    }
}
