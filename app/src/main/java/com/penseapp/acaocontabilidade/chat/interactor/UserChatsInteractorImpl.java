package com.penseapp.acaocontabilidade.chat.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.penseapp.acaocontabilidade.chat.model.Chat;
import com.penseapp.acaocontabilidade.chat.presenter.ChatsPresenter;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

/**
 * Created by unity on 21/11/16.
 */

public class UserChatsInteractorImpl implements UserChatsInteractor {

    private final static String LOG_TAG = UserChatsInteractorImpl.class.getSimpleName();

    private final ChatsPresenter chatsPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private DatabaseReference chatsReference = mFirebaseHelperInstance.getChatsReference();
    private DatabaseReference userChatsReference = mFirebaseHelperInstance.getUserChatsReference();
    private DatabaseReference currentUserChatsReference = mFirebaseHelperInstance.getCurrentUserChatsReference();

    private ChildEventListener chatsChildEventListener;

    public UserChatsInteractorImpl(ChatsPresenter chatsPresenter) {
        this.chatsPresenter = chatsPresenter;
    }

    @Override
    public void subscribeForUserChatsUpdates() {

        Log.i(LOG_TAG, "Interactor " + LOG_TAG + " called");

        if (chatsChildEventListener == null) {
            chatsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String chatKey = dataSnapshot.getKey();
                    chatsReference.child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            Log.i(LOG_TAG, "Chat " + chat.getName() + " added");
                            chatsPresenter.onChatAdded(chat);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    String chatKey = dataSnapshot.getKey();
                    chatsReference.child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            Log.i(LOG_TAG, "Chat " + chat.getName() + " changed");
                            // TODO no futuro retornar chat e key?
                            chatsPresenter.onChatChanged(chat);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String chatKey = dataSnapshot.getKey();
                    chatsReference.child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String chatId = dataSnapshot.getKey();
                            Log.i(LOG_TAG, "Chat " + chatId + " removed");
                            chatsPresenter.onChatRemoved(chatId);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.i(LOG_TAG, dataSnapshot.toString() + " moved");
//            Workout movedWorkout = dataSnapshot.getValue(Workout.class);
//            Log.i(LOG_TAG, movedWorkout.getName() + " moved");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i(LOG_TAG, "onCancelled called");
                }
            };

            currentUserChatsReference.addChildEventListener(chatsChildEventListener);
        }

    }

    @Override
    public void unsubscribeForUserChatsUpdates() {
        if (chatsChildEventListener != null) {
            currentUserChatsReference.removeEventListener(chatsChildEventListener);
        }
    }

}