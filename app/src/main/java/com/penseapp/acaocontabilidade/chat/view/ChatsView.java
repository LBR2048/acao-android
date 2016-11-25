package com.penseapp.acaocontabilidade.chat.view;

import com.penseapp.acaocontabilidade.chat.model.Chat;

/**
 * Created by unity on 21/11/16.
 */

public interface ChatsView {
    void onChatAdded(Chat chat);
    void onChatChanged(Chat chat);
    void onChatRemoved(String chatId);
}
