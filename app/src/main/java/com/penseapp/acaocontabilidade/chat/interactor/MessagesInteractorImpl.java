package com.penseapp.acaocontabilidade.chat.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private final DatabaseReference chatMessagesReference;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private ChildEventListener messagesChildEventListener;
    private DatabaseReference userChatPropertiesReference;
    private String currentChatId;

    public MessagesInteractorImpl(MessagesPresenter messagesPresenter, String currentChatId) {
        this.messagesPresenter = messagesPresenter;
        chatMessagesReference = mFirebaseHelperInstance.getChatMessagesReference();
        userChatPropertiesReference = mFirebaseHelperInstance.getUserChatPropertiesReference();
        this.currentChatId = currentChatId;
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

            chatMessagesReference.child(currentChatId).addChildEventListener(messagesChildEventListener);
        }
    }

    @Override
    public void unsubscribeForMessagesUpdates() {
        if (messagesChildEventListener != null) {
            chatMessagesReference.child(currentChatId).removeEventListener(messagesChildEventListener);
        }
    }

    @Override
    public void sendMessage(String messageText, final String senderId, String senderEmail) {
        // Create empty message at chats/$currentChatId/messages and get its key so we can further reference it
        String newMessageKey = chatMessagesReference.child(currentChatId).push().getKey();

        // Create new message with key received from Firebase
        Message newMessage = new Message();
        newMessage.setText(messageText);
        newMessage.setSenderId(senderId);
        newMessage.setSenderEmail(senderEmail);
        newMessage.setTimestamp(System.currentTimeMillis());

        // Save new messages in one node only
        // Add newly created message to Firebase chats/$currentChatId/messages/$messageId
        chatMessagesReference.child(currentChatId).child(newMessageKey).setValue(newMessage);

        // Save new messages in two nodes, one for each user
        // Add newly created message to Firebase user-chats-messages/$senderId/$currentChatId/$messageId
        // Add newly created message to Firebase user-chats-messages/$recipientId/$currentChatId/$messageId

        String currentUserId = mFirebaseHelperInstance.getAuthUserId();
        updateLatestMessageTimestamp(currentUserId);
        mFirebaseHelperInstance.getUserChatPropertiesReference().child(currentUserId).child(currentChatId).child("contactId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String contactId = dataSnapshot.getValue(String.class);
                updateLatestMessageTimestamp(contactId);
                incrementUnreadMessageCount(contactId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateLatestMessageTimestamp(String userId) {
        userChatPropertiesReference.child(userId).child(currentChatId).child("latestMessageTimestamp").setValue(- System.currentTimeMillis());
    }

    private void incrementUnreadMessageCount(final String userId) {
        final DatabaseReference unreadMessageCountPath = userChatPropertiesReference.child(userId).child(currentChatId).child("unreadMessageCount");
        unreadMessageCountPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    long unreadMessageCount = dataSnapshot.getValue(long.class);
                    unreadMessageCountPath.setValue(unreadMessageCount + 1);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Could not read unreadMessageCount");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
