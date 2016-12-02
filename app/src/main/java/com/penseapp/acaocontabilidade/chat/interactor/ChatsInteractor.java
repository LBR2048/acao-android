package com.penseapp.acaocontabilidade.chat.interactor;

/**
 * Created by Cleusa on 27/11/2016.
 */

public interface ChatsInteractor {
    void createChat(String chatName, String contacId);
    void createChatIfNeeded(String chatName, String contacId);
}
