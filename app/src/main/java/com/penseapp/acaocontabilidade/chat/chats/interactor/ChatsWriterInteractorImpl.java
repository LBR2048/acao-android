package com.penseapp.acaocontabilidade.chat.chats.interactor;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.penseapp.acaocontabilidade.chat.chats.model.Chat;
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsWriterPresenter;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

/**
 * Created by Cleusa on 27/11/2016.
 */

public class ChatsWriterInteractorImpl implements ChatsWriterInteractor {

    private final static String LOG_TAG = ChatsWriterInteractorImpl.class.getSimpleName();

    private final ChatsWriterPresenter chatsWriterPresenter;

    // Firebase
    private final FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private final DatabaseReference userChatContactsReference = mFirebaseHelperInstance.getUserChatContactsReference();

    private final String currentUserId = mFirebaseHelperInstance.getAuthUserId();
    private String newChatKey;

    public ChatsWriterInteractorImpl(ChatsWriterPresenter chatsWriterPresenter) {
        this.chatsWriterPresenter = chatsWriterPresenter;
    }

    private void createChat(String senderId, String senderName, String senderCompany, String recipientId, String recipientName, String recipientCompany) {

        DatabaseReference userChatPropertiesReference = mFirebaseHelperInstance.getUserChatPropertiesReference();

        // Create empty chat and get its key so we can further reference it
        newChatKey = userChatPropertiesReference.child(senderId).push().getKey();

        // Create new chat with key received from Firebase
        Chat newChat = new Chat();
        newChat.setContactId(recipientId);
        newChat.setContactCompany(recipientCompany);
        newChat.setName(recipientName);
        userChatPropertiesReference.child(senderId).child(newChatKey).setValue(newChat);

        newChat.setContactId(senderId);
        newChat.setContactCompany(senderCompany);
        newChat.setName(senderName);
        userChatPropertiesReference.child(recipientId).child(newChatKey).setValue(newChat);

        // Add reference to newly created chat to user-chatContacts-chat/$currentUserId/$contactId:$newChatId
        userChatContactsReference.child(currentUserId).child(recipientId).setValue(newChatKey);

        // Add reference to newly created chat to user-chatContacts-chat/$contactId/$currentUserId:$newChatId
        userChatContactsReference.child(recipientId).child(currentUserId).setValue(newChatKey);

        // TODO Creio que é útil apenas para grupos. Retirar por enquanto.
        // For each $userId:
        // Create chat-users/$userId
//        mFirebaseHelperInstance.getChatUsersReference().child(newChatKey).child(senderId).setValue(true);
//        mFirebaseHelperInstance.getChatUsersReference().child(newChatKey).child(recipientId).setValue(true);

        // TODO Creio que é útil apenas para grupos. Retirar por enquanto.
        // For each $userId:
        // Create chat at user-chats-properties/$userId/$chatId
//        mFirebaseHelperInstance.getUserChatPropertiesReference().child(senderId).child(newChatKey).setValue(newChat);
//        mFirebaseHelperInstance.getUserChatPropertiesReference().child(recipientId).child(newChatKey).setValue(newChat);

        // TODO Creio que é útil apenas para grupos. Retirar por enquanto.
        // For each $userId:
        // Create user-chats-properties/users/$userId
//        mFirebaseHelperInstance.getUserChatPropertiesReference().child(senderId).child(newChatKey).child("users").child(senderId).setValue(true);
//        mFirebaseHelperInstance.getUserChatPropertiesReference().child(senderId).child(newChatKey).child("users").child(recipientId).setValue(true);
//        mFirebaseHelperInstance.getUserChatPropertiesReference().child(recipientId).child(newChatKey).child("users").child(senderId).setValue(true);
//        mFirebaseHelperInstance.getUserChatPropertiesReference().child(recipientId).child(newChatKey).child("users").child(recipientId).setValue(true);

    }

    @Override
    public void createChatIfNeeded(final String senderId, final String senderName, final String senderCompany, final String recipientId, final String recipientName, final String recipientCompany) {
        userChatContactsReference.child(currentUserId).child(recipientId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Object dataSnapshotValue = dataSnapshot.getValue();
                        if (dataSnapshotValue == null) {
                            createChat(senderId, senderName, senderCompany, recipientId, recipientName, recipientCompany);
                            chatsWriterPresenter.onChatCreated(newChatKey, recipientName);
                        } else {
                            chatsWriterPresenter.onChatCreated(dataSnapshotValue.toString(), recipientName);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
