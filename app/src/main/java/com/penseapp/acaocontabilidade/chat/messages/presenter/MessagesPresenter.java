package com.penseapp.acaocontabilidade.chat.messages.presenter;

import android.net.Uri;

import com.penseapp.acaocontabilidade.chat.messages.model.Message;

/**
 * Created by unity on 21/11/16.
 */

public interface MessagesPresenter {
    void subscribeForMessagesUpdates();
    void unsubscribeForMessagesUpdates();
    void resetUnreadMessageCount(String chatId);
    void onMessageAdded(Message message);
    void sendMessage(String messageText, String senderId, String senderEmail, Uri fileUri);
}
