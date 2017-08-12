package com.penseapp.acaocontabilidade.chat.chats.presenter;

import com.penseapp.acaocontabilidade.chat.chats.model.Chat;

/**
 * Created by unity on 10/12/16.
 */

public interface ChatsReaderPresenter {
    void subscribeForChatListUpdates();
    void unsubscribeForChatListUpdates();
    void onChatAdded(Chat chat);
    void onChatChanged(Chat chat);
    void onChatRemoved(String chatId);
}
