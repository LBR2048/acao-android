package com.penseapp.acaocontabilidade.chat.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.penseapp.acaocontabilidade.chat.model.Message;
import com.penseapp.acaocontabilidade.chat.presenter.MessagesPresenter;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;


/**
 * Created by unity on 21/11/16.
 */

public class MessagesInteractorImpl implements MessagesInteractor {

    private final static String LOG_TAG = ContactsInteractorImpl.class.getSimpleName();

    private final MessagesPresenter messagesPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private DatabaseReference currentChatReference;
    private DatabaseReference currentChatMessagesReference;
    private DatabaseReference currentChatUserChatsReference;
    private ChildEventListener messagesChildEventListener;

    public MessagesInteractorImpl(MessagesPresenter messagesPresenter, String chatId) {
        this.messagesPresenter = messagesPresenter;
        currentChatReference = mFirebaseHelperInstance.getCurrentChatReference(chatId);
        currentChatMessagesReference = mFirebaseHelperInstance.getCurrentChatMessagesReference(chatId);
        currentChatUserChatsReference = mFirebaseHelperInstance.getCurrentUserChatsReference().child(chatId);
    }

    @Override
    public void subscribeForMessagesUpdates() {
        Log.i(LOG_TAG, "Interactor " + LOG_TAG + " called");

        if (messagesChildEventListener == null) {
            messagesChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String messageKey = dataSnapshot.getKey();
                    try {
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setKey(messageKey);
                        Log.i(LOG_TAG, dataSnapshot.toString() + " added");
                        messagesPresenter.onMessageAdded(message);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error while reading message " + messageKey);
                        Message message = new Message();
                        messagesPresenter.onMessageAdded(message);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            // TODO colocar dentro do FirebaseHelper
            currentChatMessagesReference.addChildEventListener(messagesChildEventListener);
        }
    }

    @Override
    public void unsubscribeForMessagesUpdates() {
        if (messagesChildEventListener != null) {
            currentChatMessagesReference.removeEventListener(messagesChildEventListener);
        }
    }

    @Override
    public void sendMessage(String messageText, String senderId, String senderName) {
        // Create empty messageText and get its key so we can further reference it
        String newMessageKey = currentChatMessagesReference.push().getKey();

        // Create new messageText with key received from Firebase
        Message newMessage = new Message();
        newMessage.setText(messageText);
        newMessage.setSenderId(senderId);
        newMessage.setSenderName(senderName);
        newMessage.setTimestamp(System.currentTimeMillis());

        // Add newly created message to Firebase chats/$chatId/$messageId
        currentChatMessagesReference.child(newMessageKey).setValue(newMessage);

        // Update current chat's latestMessageTimestamp and unreadMessageCount
        currentChatReference.child("latestMessageTimestamp").setValue(ServerValue.TIMESTAMP);
        currentChatReference.child("unreadMessageCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    long timestamp = dataSnapshot.getValue(long.class);
                    currentChatReference.child("unreadMessageCount").setValue(timestamp + 1);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Could not read unreadMessageCount");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // Update timestamp at user-chats/$userId/$chatId:timestamp
        currentChatUserChatsReference.setValue(ServerValue.TIMESTAMP);
    }
}
