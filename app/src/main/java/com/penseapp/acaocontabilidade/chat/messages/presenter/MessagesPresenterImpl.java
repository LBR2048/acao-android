package com.penseapp.acaocontabilidade.chat.messages.presenter;

import android.net.Uri;

import com.penseapp.acaocontabilidade.chat.messages.interactor.MessagesInteractor;
import com.penseapp.acaocontabilidade.chat.messages.interactor.MessagesInteractorImpl;
import com.penseapp.acaocontabilidade.chat.messages.model.Message;
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesView;

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
    public void resetUnreadMessageCount(String chatId) {
        messagesInteractor.resetUnreadMessageCount(chatId);
    }

    @Override
    public void onMessageAdded(Message message) {
        messagesView.onMessageAdded(message);
    }

    @Override
    public void onMessageChanged(Message message) {
        messagesView.onMessageChanged(message);
    }

    @Override
    public void sendMessage(String messageText, String senderId, String senderEmail, Uri imageUri, Uri documentUri) {
        messagesInteractor.sendMessage(messageText, senderId, senderEmail, imageUri, documentUri);
    }
}
