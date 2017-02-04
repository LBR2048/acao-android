package com.penseapp.acaocontabilidade.chat.adapters;

import com.penseapp.acaocontabilidade.chat.model.Chat;

/**
 * Created by unity on 03/02/17.
 */

public interface ChatsAdapterView {
    void subscribeForChatsUpdates();
    void unsubscribeForChatListUpdates();
    void onChatAdded(Chat chat);
    void onChatChanged(Chat chat);
    void onChatRemoved(String chatId);
}
