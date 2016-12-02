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
 * Created by Cleusa on 27/11/2016.
 */

public class ChatUsersInteractorImpl implements ChatUsersInteractor {

    private final static String LOG_TAG = ChatUsersInteractorImpl.class.getSimpleName();

    private final ChatsPresenter chatsPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private DatabaseReference chatsReference = mFirebaseHelperInstance.getChatsReference();
    private DatabaseReference chatUsersReference = mFirebaseHelperInstance.getChatUsersReference();
    private DatabaseReference currentChatUsersReference = mFirebaseHelperInstance.getCurrentChatUsersReference();

    private ChildEventListener chatUsersChildEventListener;

    public ChatUsersInteractorImpl(ChatsPresenter chatsPresenter) {
        this.chatsPresenter = chatsPresenter;
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
