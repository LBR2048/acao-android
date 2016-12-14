package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.adapters.ContactsAdapterView;
import com.penseapp.acaocontabilidade.chat.interactor.ChatsInteractor;
import com.penseapp.acaocontabilidade.chat.interactor.ChatsInteractorImpl;
import com.penseapp.acaocontabilidade.chat.view.ChatsView;
import com.penseapp.acaocontabilidade.chat.view.ContactsView;
import com.penseapp.acaocontabilidade.chat.view.TabbedMainActivity;

/**
 * Created by unity on 21/11/16.
 */

public class ChatsPresenterImpl implements ChatsPresenter {

    private final ChatsView chatsView;
    private final ContactsView contactsView;
    private final TabbedMainActivity tabbedMainActivity;
    private final ChatsInteractor chatsInteractor;
    private final ContactsAdapterView contactsAdapterView;

    public ChatsPresenterImpl(ChatsView chatsView) {
        this.contactsView = null;
        this.chatsView = chatsView;
        this.tabbedMainActivity = null;
        this.contactsAdapterView = null;
        this.chatsInteractor = new ChatsInteractorImpl(this);
    }

//    public ChatsPresenterImpl(TabbedMainActivity tabbedMainActivity) {
//        this.contactsView = null;
//        this.chatsView = null;
//        this.tabbedMainActivity = tabbedMainActivity;
//        this.contactsAdapterView = null;
//        this.chatsInteractor = new ChatsInteractorImpl(this);
//    }

    public ChatsPresenterImpl(ContactsView contactsView) {
        this.contactsView = contactsView;
        this.chatsView = null;
        this.tabbedMainActivity = null;
        this.contactsAdapterView = null;
        this.chatsInteractor = new ChatsInteractorImpl(this);
    }

    public ChatsPresenterImpl(ContactsAdapterView contactsAdapterView) {
        this.contactsView = null;
        this.chatsView = null;
        this.tabbedMainActivity = null;
        this.contactsAdapterView = contactsAdapterView;
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
