package com.penseapp.acaocontabilidade.chat.interactor;

/**
 * Created by unity on 21/11/16.
 */

public interface ChatListInteractor {
    void subscribeForChatListUpdates();
    void unsubscribeForChatListUpdates();
    void createChat(String chatName, String contacId);
}
