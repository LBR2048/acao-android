package com.penseapp.acaocontabilidade.chat.presenter;

/**
 * Created by unity on 21/11/16.
 */

public interface ChatsPresenter {
    void createChat(String chatName, String contactId);
    void createChatIfNeeded(String chatName, String contactId);
    void onChatCreated(String chatId, String chatName);
}
