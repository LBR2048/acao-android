package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.adapters.ChatsAdapterView;
import com.penseapp.acaocontabilidade.chat.interactor.ChatsReaderInteractor;
import com.penseapp.acaocontabilidade.chat.interactor.ChatsReaderInteractorImpl;
import com.penseapp.acaocontabilidade.chat.model.Chat;

/**
 * Created by unity on 10/12/16.
 */

public class ChatsReaderPresenterImpl implements ChatsReaderPresenter {

    private final ChatsAdapterView chatsView;
    private final ChatsReaderInteractor chatsReaderInteractor;

    public ChatsReaderPresenterImpl(ChatsAdapterView chatsAdapterView) {
        this.chatsView = chatsAdapterView;
        this.chatsReaderInteractor = new ChatsReaderInteractorImpl(this);
    }

    @Override
    public void subscribeForChatListUpdates() {
        chatsReaderInteractor.subscribeForUserChatsUpdates();
    }

    @Override
    public void unsubscribeForChatListUpdates() {
        chatsReaderInteractor.unsubscribeForUserChatsUpdates();
    }

    @Override
    public void onChatAdded(Chat chat) {
        chatsView.onChatAdded(chat);
    }

    @Override
    public void onChatChanged(Chat chat) {
        chatsView.onChatChanged(chat);
    }

    @Override
    public void onChatRemoved(String chatId) {
        chatsView.onChatRemoved(chatId);
    }
}
