package com.penseapp.acaocontabilidade.chat.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.penseapp.acaocontabilidade.chat.model.Chat;
import com.penseapp.acaocontabilidade.chat.presenter.ChatListPresenter;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

/**
 * Created by unity on 21/11/16.
 */

public class ChatListInteractorImpl implements ChatListInteractor {

    private final static String LOG_TAG = ChatListInteractorImpl.class.getSimpleName();

    private final ChatListPresenter chatListPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private DatabaseReference chatsReference = mFirebaseHelperInstance.getChatsReference();
    private DatabaseReference userChatsReference = mFirebaseHelperInstance.getUserChatsReference();
    private DatabaseReference currentUserChatsReference = mFirebaseHelperInstance.getCurrentUserChatsReference();

    private ChildEventListener chatsChildEventListener;

    public ChatListInteractorImpl(ChatListPresenter chatListPresenter) {
        this.chatListPresenter = chatListPresenter;
    }

    @Override
    public void subscribeForChatListUpdates() {

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
                            chatListPresenter.onChatAdded(chat);
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
                            chatListPresenter.onChatChanged(chat);
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
                            chatListPresenter.onChatRemoved(chatId);
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
    public void unsubscribeForChatListUpdates() {
        if (chatsChildEventListener != null) {
            currentUserChatsReference.removeEventListener(chatsChildEventListener);
        }
    }

    @Override
    public void createChat(String chatName, String contactId) {
        // Create empty chat and get its key so we can further reference it
        String newChatKey = chatsReference.push().getKey();

        // Create new chat with key received from Firebase
        Chat newChat = new Chat();
        newChat.setKey(newChatKey);
        newChat.setName("Chat com " + chatName);

        // Add newly created chat to Firebase chats/$chatId
        chatsReference.child(newChatKey).setValue(newChat);

        // Add reference to newly created chat at user-chats/$currentUserId/$chatId
        currentUserChatsReference.child(newChatKey).setValue(ServerValue.TIMESTAMP);

        // Add reference to newly created chat at user-chats/$contactId/$chatId
        userChatsReference.child(contactId).child(newChatKey).setValue(ServerValue.TIMESTAMP);
    }
}