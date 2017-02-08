package com.penseapp.acaocontabilidade.chat.interactor;

/**
 * Created by Cleusa on 27/11/2016.
 */

public interface ChatsInteractor {
    void createChat(String recipientName, String recipientId);
    void createChatIfNeeded(String recipientName, String recipientId);
}
