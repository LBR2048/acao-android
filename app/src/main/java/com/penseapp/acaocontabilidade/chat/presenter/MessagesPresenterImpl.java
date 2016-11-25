package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.interactor.MessagesInteractor;
import com.penseapp.acaocontabilidade.chat.interactor.MessagesInteractorImpl;
import com.penseapp.acaocontabilidade.chat.model.Message;
import com.penseapp.acaocontabilidade.chat.view.MessagesView;

/**
 * Created by unity on 21/11/16.
 */

public class MessagesPresenterImpl implements MessagesPresenter {

    private final MessagesView messagesView;
    private final MessagesInteractor messagesInteractor;

    public MessagesPresenterImpl(MessagesView messagesView, String chatId) {
        this.messagesView = messagesView;
        this.messagesInteractor = new MessagesInteractorImpl(this, chatId);
    }

    @Override
    public void subscribeForMessagesUpdates() {
        messagesInteractor.subscribeForMessagesUpdates();
    }

    @Override
    public void unsubscribeForMessagesUpdates() {
        messagesInteractor.unsubscribeForMessagesUpdates();
    }

    @Override
    public void onMessageAdded(Message message) {
        messagesView.onMessageAdded(message);
    }

    @Override
    public void sendMessage(String messageText, String senderId, String senderName) {
        messagesInteractor.sendMessage(messageText, senderId, senderName);
    }
}
