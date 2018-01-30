package com.penseapp.acaocontabilidade.chat.messages.interactor;

/**
 * Created by unity on 21/11/16.
 */

public interface MessagesInteractor {
    void subscribeForMessagesUpdates();
    void unsubscribeForMessagesUpdates();
    void resetUnreadMessageCount(String chatId);
    void sendMessage(String messageText, String senderId, String senderEmail, String photoURL);
}
