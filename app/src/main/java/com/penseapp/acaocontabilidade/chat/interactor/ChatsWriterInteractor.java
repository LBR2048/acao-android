package com.penseapp.acaocontabilidade.chat.interactor;

/**
 * Created by Cleusa on 27/11/2016.
 */

public interface ChatsWriterInteractor {
    void createChatIfNeeded(String senderId, String senderName, String recipientId, String recipientName);
}
