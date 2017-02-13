package com.penseapp.acaocontabilidade.chat.presenter;

/**
 * Created by unity on 21/11/16.
 */

public interface ChatsPresenter {
    void createChatIfNeeded(String senderId, String senderName, String recipientId, String recipientName);
    void onChatCreated(String chatId, String chatName);
}
