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
 * Created by Cleusa on 27/11/2016.
 */

public class UserChatContactsInteractorImpl implements UserChatContactsInteractor {

    private final static String LOG_TAG = UserChatContactsInteractorImpl.class.getSimpleName();

    private final UserChatsPresenter userChatsPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private DatabaseReference chatsReference = mFirebaseHelperInstance.getChatsReference();
    private DatabaseReference currentChatUsersReference = mFirebaseHelperInstance.getCurrentChatUsersReference();

    private ChildEventListener chatUsersChildEventListener;

    public UserChatContactsInteractorImpl(UserChatsPresenter userChatsPresenter) {
        this.userChatsPresenter = userChatsPresenter;
    }

    @Override
    public void subscribeForChatUsersUpdates() {

        Log.i(LOG_TAG, "Interactor " + LOG_TAG + " called");

        if (chatUsersChildEventListener== null) {
            chatUsersChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String chatKey = dataSnapshot.getKey();
                    chatsReference.child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            chat.setKey(dataSnapshot.getKey());
                            Log.i(LOG_TAG, "Chat " + chat.getName() + " added");
                            userChatsPresenter.onChatAdded(chat);
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
                            chat.setKey(dataSnapshot.getKey());
                            userChatsPresenter.onChatChanged(chat);
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
                            userChatsPresenter.onChatRemoved(chatId);
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

            currentChatUsersReference.addChildEventListener(chatUsersChildEventListener);
        }
    }

    @Override
    public void unsubscribeForChatUsersUpdates() {
        if (chatUsersChildEventListener== null) {
            currentChatUsersReference.removeEventListener(chatUsersChildEventListener);
        }
    }
}
