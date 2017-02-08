package com.penseapp.acaocontabilidade.chat.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.penseapp.acaocontabilidade.chat.model.Chat;
import com.penseapp.acaocontabilidade.chat.presenter.UserChatsPresenter;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

/**
 * Created by unity on 21/11/16.
 */

public class UserChatsInteractorImpl implements UserChatsInteractor {

    private final static String LOG_TAG = UserChatsInteractorImpl.class.getSimpleName();

    private final UserChatsPresenter userChatsPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private DatabaseReference chatsReference = mFirebaseHelperInstance.getChatsReference();
    private DatabaseReference userChatsReference = mFirebaseHelperInstance.getUserChatsReference();
    private DatabaseReference currentUserChatsReference = mFirebaseHelperInstance.getCurrentUserChatsReference();

    private ChildEventListener chatsChildEventListener;

    public UserChatsInteractorImpl(UserChatsPresenter userChatsPresenter) {
        this.userChatsPresenter = userChatsPresenter;
    }

    @Override
    public void subscribeForUserChatsUpdates() {

        Log.i(LOG_TAG, "Interactor " + LOG_TAG + " called");

        if (chatsChildEventListener == null) {
            chatsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final String chatKey = dataSnapshot.getKey();
                    chatsReference.child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                Chat chat = dataSnapshot.getValue(Chat.class);
                                chat.setKey(dataSnapshot.getKey());
                                Log.i(LOG_TAG, "Chat " + chat.getName() + " added");
                                userChatsPresenter.onChatAdded(chat);
                            } catch (Exception e) {
                                Log.e(LOG_TAG, "Error while reading chat " + chatKey);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    final String chatKey = dataSnapshot.getKey();
                    chatsReference.child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                Chat chat = dataSnapshot.getValue(Chat.class);
                                Log.i(LOG_TAG, "Chat " + chat.getName() + " changed");
                                chat.setKey(dataSnapshot.getKey());
                                userChatsPresenter.onChatChanged(chat);
                            } catch (Exception e) {
                                Log.e(LOG_TAG, "Error while reading chat " + chatKey);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    final String chatKey = dataSnapshot.getKey();
                    chatsReference.child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                String chatId = dataSnapshot.getKey();
                                Log.i(LOG_TAG, "Chat " + chatId + " removed");
                                userChatsPresenter.onChatRemoved(chatId);
                            } catch (Exception e) {
                                Log.e(LOG_TAG, "Error while reading chat " + chatKey);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

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