package com.penseapp.acaocontabilidade.chat.presenter;

/**
 * Created by unity on 21/11/16.
 */

public interface ChatsWriterPresenter {
    void createChatIfNeeded(String senderId, String senderName, String senderCompany, String recipientId, String recipientName, String recipientCompany);
    void onChatCreated(String chatId, String chatName);
}
