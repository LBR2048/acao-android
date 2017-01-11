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
    private DatabaseReference userChatsReference = mFirebaseHelperInstance.getUserChatsReference();
    private DatabaseReference userChatContactsReference = mFirebaseHelperInstance.getUserChatContactsReference();
    private DatabaseReference chatsReference = mFirebaseHelperInstance.getChatsReference();
    private DatabaseReference currentUserChatsReference = mFirebaseHelperInstance.getCurrentUserChatsReference();
    private DatabaseReference chatUsersReference = mFirebaseHelperInstance.getChatUsersReference();
    private String currentUserId = mFirebaseHelperInstance.getAuthUserId();
    private ValueEventListener userChatsChildEventListener;
    private String newChatKey;


    public ChatsInteractorImpl(ChatsPresenter chatsPresenter) {
        this.chatsPresenter = chatsPresenter;
    }

    @Override
    public void createChat(String chatName, String contactId) {
        // Create empty chat and get its key so we can further reference it
        newChatKey = chatsReference.push().getKey();

        // Create new chat with key received from Firebase
        Chat newChat = new Chat();
        newChat.setKey(newChatKey);
        newChat.setName("Chat com " + chatName);

        // Add newly created chat to Firebase chats/$chatId
        chatsReference.child(newChatKey).setValue(newChat);

        // Add reference to newly created chat to user-chats/$currentUserId/$chatId
        currentUserChatsReference.child(newChatKey).setValue(ServerValue.TIMESTAMP);

        // Add reference to newly created chat to user-chats/$contactId/$chatId
        userChatsReference.child(contactId).child(newChatKey).setValue(ServerValue.TIMESTAMP);

        // Add reference to contactId to user-chatContacts-chat/$currentUserId/$contactId
        // TODO acho que não é necessário
        userChatContactsReference.child(currentUserId).child(contactId).setValue(ServerValue.TIMESTAMP);

        // Add reference to contactId to user-chatContacts-chat/$contactId/$currentUserId
        // TODO acho que não é necessário
        userChatContactsReference.child(contactId).child(currentUserId).setValue(ServerValue.TIMESTAMP);

        // Add reference to newly created chat to user-chatContacts-chat/$currentUserId/$contactId:$newChatId
        userChatContactsReference.child(currentUserId).child(contactId).setValue(newChatKey);

        // Add reference to newly created chat to user-chatContacts-chat/$contactId/$currentUserId:$newChatId
        userChatContactsReference.child(contactId).child(currentUserId).setValue(newChatKey);


        // TODO remove chat-users tree
        // Add reference to newly created chat to chat-users/$chatId/$currentUserId
//        chatUsersReference.child(newChatKey).child(currentUserId).setValue(ServerValue.TIMESTAMP);

        // TODO remove chat-users tree
        // Add reference to newly created chat to chat-users/$chatId/$contactId
//        chatUsersReference.child(newChatKey).child(contactId).setValue(ServerValue.TIMESTAMP);

    }

    @Override
    public void createChatIfNeeded(final String chatName, final String contactId) {
        if (userChatsChildEventListener == null) {
            userChatsChildEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.i(LOG_TAG, "Current user chats");
//                    Log.i(LOG_TAG, userChatContactsReference.child(currentUserId).child(contactId).toString());
//                    Log.i(LOG_TAG, dataSnapshot.toString());
                    Object dataSnapshotValue = dataSnapshot.getValue();
                    if (dataSnapshotValue == null) {
                        createChat(chatName, contactId);
                        chatsPresenter.onChatCreated(newChatKey, chatName);
                    } else {
                        chatsPresenter.onChatCreated(dataSnapshotValue.toString(), chatName);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            userChatContactsReference.child(currentUserId).child(contactId).addListenerForSingleValueEvent(userChatsChildEventListener);
        }
    }
}
