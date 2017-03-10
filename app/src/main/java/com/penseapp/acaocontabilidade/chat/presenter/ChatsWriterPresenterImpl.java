package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.interactor.ChatsWriterInteractor;
import com.penseapp.acaocontabilidade.chat.interactor.ChatsWriterInteractorImpl;
import com.penseapp.acaocontabilidade.chat.view.ContactsView;

/**
 * Created by unity on 21/11/16.
 */

public class ChatsWriterPresenterImpl implements ChatsWriterPresenter {

    private final ContactsView contactsView;
    private final ChatsWriterInteractor chatsWriterInteractor;

    public ChatsWriterPresenterImpl(ContactsView contactsView) {
        this.contactsView = contactsView;
        this.chatsWriterInteractor = new ChatsWriterInteractorImpl(this);
    }

    @Override
    public void createChatIfNeeded(String senderId, String senderName, String senderCompany, String recipientId, String recipientName, String recipientCompany) {
        chatsWriterInteractor.createChatIfNeeded(senderId, senderName, senderCompany, recipientId, recipientName, recipientCompany);
    }

    @Override
    public void onChatCreated(String chatId, String chatName) {
        contactsView.onChatCreated(chatId, chatName);
    }
}
