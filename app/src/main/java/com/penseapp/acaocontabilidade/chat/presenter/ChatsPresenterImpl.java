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
    public void createChat(String chatName, String contactId) {
        chatsInteractor.createChat(chatName, contactId);
    }

    @Override
    public void createChatIfNeeded(String chatName, String contactId) {
        chatsInteractor.createChatIfNeeded(chatName, contactId);
    }

    @Override
    public void onChatCreated(String chatId, String chatName) {
        contactsView.onChatCreated(chatId, chatName);
    }
}
