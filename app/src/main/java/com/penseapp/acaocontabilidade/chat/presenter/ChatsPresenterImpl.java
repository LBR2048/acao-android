package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.interactor.ChatsInteractor;
import com.penseapp.acaocontabilidade.chat.interactor.ChatsInteractorImpl;
import com.penseapp.acaocontabilidade.chat.view.ContactsView;

/**
 * Created by unity on 21/11/16.
 */

public class ChatsPresenterImpl implements ChatsPresenter {

    private final ContactsView contactsView;
    private final ChatsInteractor chatsInteractor;

    public ChatsPresenterImpl(ContactsView contactsView) {
        this.contactsView = contactsView;
        this.chatsInteractor = new ChatsInteractorImpl(this);
    }

    @Override
    public void createChatIfNeeded(String senderId, String senderName, String recipientId, String recipientName) {
        chatsInteractor.createChatIfNeeded(senderId, senderName, recipientId, recipientName);
    }

    @Override
    public void onChatCreated(String chatId, String chatName) {
        contactsView.onChatCreated(chatId, chatName);
    }
}
