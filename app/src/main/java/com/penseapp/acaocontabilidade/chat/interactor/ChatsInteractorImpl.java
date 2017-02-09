package com.penseapp.acaocontabilidade.chat.interactor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.penseapp.acaocontabilidade.chat.model.Chat;
import com.penseapp.acaocontabilidade.chat.presenter.ChatsPresenter;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

/**
 * Created by Cleusa on 27/11/2016.
 */

public class ChatsInteractorImpl implements ChatsInteractor {

    private final static String LOG_TAG = ChatsInteractorImpl.class.getSimpleName();

    private final ChatsPresenter chatsPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private DatabaseReference userChatContactsReference = mFirebaseHelperInstance.getUserChatContactsReference();
    private DatabaseReference chatsReference = mFirebaseHelperInstance.getChatsReference();
    private DatabaseReference currentUserChatsReference = mFirebaseHelperInstance.getCurrentUserChatsReference();
    private DatabaseReference userChatsReference = mFirebaseHelperInstance.getUserChatsReference();

    private String currentUserId = mFirebaseHelperInstance.getAuthUserId();
    private ValueEventListener userChatsChildEventListener;
    private String newChatKey;


    public ChatsInteractorImpl(ChatsPresenter chatsPresenter) {
        this.chatsPresenter = chatsPresenter;
    }

    @Override
    public void createChat(final String recipientName, String recipientId) {
        // Create empty chat and get its key so we can further reference it
        newChatKey = chatsReference.push().getKey();

        // Create new chat with key received from Firebase
        final Chat newChat = new Chat();
        newChat.setFirstUserId(mFirebaseHelperInstance.getAuthUserId());
        newChat.setSecondUserId(recipientId);
        newChat.setSecondUserName(recipientName);

        mFirebaseHelperInstance.getUsersReference().child(mFirebaseHelperInstance.getAuthUserId()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String senderName = dataSnapshot.getValue(String.class);
                newChat.setName("Chat entre " + senderName + " e " + recipientName);
                newChat.setFirstUserName(senderName);

                // Add newly created chat to Firebase chats/$chatId
                chatsReference.child(newChatKey).setValue(newChat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Add reference to newly created chat to user-chats/$currentUserId/$chatId
        userChatsReference.child(currentUserId).child(newChatKey).setValue(ServerValue.TIMESTAMP);

        // Add reference to newly created chat to user-chats/$contactId/$chatId
        userChatsReference.child(recipientId).child(newChatKey).setValue(ServerValue.TIMESTAMP);

        // Add reference to newly created chat to user-chatContacts-chat/$currentUserId/$contactId:$newChatId
        userChatContactsReference.child(currentUserId).child(recipientId).setValue(newChatKey);

        // Add reference to newly created chat to user-chatContacts-chat/$contactId/$currentUserId:$newChatId
        userChatContactsReference.child(recipientId).child(currentUserId).setValue(newChatKey);
    }

    @Override
    public void createChatIfNeeded(final String recipientName, final String recipientId) {
        if (userChatsChildEventListener == null) {
            userChatsChildEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object dataSnapshotValue = dataSnapshot.getValue();
                    if (dataSnapshotValue == null) {
                        createChat(recipientName, recipientId);
                        chatsPresenter.onChatCreated(newChatKey, recipientName);
                    } else {
                        chatsPresenter.onChatCreated(dataSnapshotValue.toString(), recipientName);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            userChatContactsReference.child(currentUserId).child(recipientId).addListenerForSingleValueEvent(userChatsChildEventListener);
        }
    }
}
