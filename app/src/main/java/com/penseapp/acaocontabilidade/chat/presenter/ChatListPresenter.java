package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.model.Chat;

/**
 * Created by unity on 21/11/16.
 */

public interface ChatListPresenter {
    void subscribeForChatListUpdates();
    void unsubscribeForChatListUpdates();
    void createChat(String chatName, String contactId);
    void onChatAdded(Chat chat);
    void onChatChanged(Chat chat);
    void onChatRemoved(String chatId);
}
