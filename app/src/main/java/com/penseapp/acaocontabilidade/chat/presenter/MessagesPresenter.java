package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.model.Message;

/**
 * Created by unity on 21/11/16.
 */

public interface MessagesPresenter {
    void subscribeForMessagesUpdates();
    void unsubscribeForMessagesUpdates();
    void onMessageAdded(Message message);
    void sendMessage(String messageText, String senderId, String senderEmail);
}
