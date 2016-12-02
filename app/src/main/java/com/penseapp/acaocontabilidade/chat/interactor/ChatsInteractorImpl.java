package com.penseapp.acaocontabilidade.chat.interactor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
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
    private DatabaseReference chatsReference = mFirebaseHelperInstance.getChatsReference();
    private DatabaseReference currentUserChatsReference = mFirebaseHelperInstance.getCurrentUserChatsReference();
    private DatabaseReference chatUsersReference = mFirebaseHelperInstance.getChatUsersReference();
    private String currentUserId = mFirebaseHelperInstance.getAuthUserId();

    public ChatsInteractorImpl(ChatsPresenter chatsPresenter) {
        this.chatsPresenter = chatsPresenter;
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

        // Add reference to newly created chat to user-chats/$currentUserId/$chatId
        currentUserChatsReference.child(newChatKey).setValue(ServerValue.TIMESTAMP);

        // Add reference to newly created chat to user-chats/$contactId/$chatId
        userChatsReference.child(contactId).child(newChatKey).setValue(ServerValue.TIMESTAMP);

        // Add reference to newly created chat to chat-users/$chatId/$currentUserId
        chatUsersReference.child(newChatKey).child(currentUserId).setValue(ServerValue.TIMESTAMP);

        // Add reference to newly created chat to chat-users/$chatId/$contactId
        chatUsersReference.child(newChatKey).child(contactId).setValue(ServerValue.TIMESTAMP);
    }

    @Override
    public void createChatIfNeeded(String chatName, String contacId) {
//        // For each chat from currentUserID
//        currentUserChats
//        for (Chat currentUserChat : currentUserChats) {
//            // For each chat from contactId
//            for (Chat contactChat : contactChats) {
//                if (currentUserChat.equals(contactChat))
//                    return;
//            }
//        }
//        chatsPresenter.createChat(chatName, contactId);


    }


}
