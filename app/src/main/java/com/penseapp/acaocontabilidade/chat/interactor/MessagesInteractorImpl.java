package com.penseapp.acaocontabilidade.chat.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private ChildEventListener messagesChildEventListener;

    public MessagesInteractorImpl(MessagesPresenter messagesPresenter, String chatId) {
        this.messagesPresenter = messagesPresenter;
        currentChatReference = mFirebaseHelperInstance.getCurrentChatReference(chatId);
    }

    @Override
    public void subscribeForMessagesUpdates() {
        Log.i(LOG_TAG, "Interactor " + LOG_TAG + " called");

        if (messagesChildEventListener == null) {
            messagesChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i(LOG_TAG, dataSnapshot.toString() + " added");
                    Message message = dataSnapshot.getValue(Message.class);
                    message.setKey(dataSnapshot.getKey());
                    messagesPresenter.onMessageAdded(message);
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
            currentChatReference.addChildEventListener(messagesChildEventListener);
        }
    }

    @Override
    public void unsubscribeForMessagesUpdates() {
        if (messagesChildEventListener != null) {
            currentChatReference.removeEventListener(messagesChildEventListener);
        }
    }

    @Override
    public void sendMessage(String messageText, String senderId, String senderName) {
        // Create empty messageText and get its key so we can further reference it
        String newMessageKey = currentChatReference.push().getKey();

        // Create new messageText with key received from Firebase
        Message newMessage = new Message();
//        newMessage.setKey(newMessageKey);
        newMessage.setText(messageText);
        newMessage.setSenderId(senderId);
        newMessage.setSenderName(senderName);
//        newMessage.setTimestamp(Long.toString(System.currentTimeMillis()));
        newMessage.setTimestamp(System.currentTimeMillis());

        // Add newly created message to Firebase chats/$chatId/$messageId
        currentChatReference.child(newMessageKey).setValue(newMessage);
    }
}
