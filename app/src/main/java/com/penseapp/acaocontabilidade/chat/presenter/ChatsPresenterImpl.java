package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.interactor.ChatsInteractor;
import com.penseapp.acaocontabilidade.chat.interactor.ChatsInteractorImpl;
import com.penseapp.acaocontabilidade.chat.interactor.UserChatsInteractor;
import com.penseapp.acaocontabilidade.chat.interactor.UserChatsInteractorImpl;
import com.penseapp.acaocontabilidade.chat.model.Chat;
import com.penseapp.acaocontabilidade.chat.view.ChatsView;

/**
 * Created by unity on 21/11/16.
 */

public class ChatsPresenterImpl implements ChatsPresenter {

    private final ChatsView chatsView;
    private final UserChatsInteractor userChatsInteractor;
    private final ChatsInteractor chatsInteractor;

    public ChatsPresenterImpl(ChatsView chatsView) {
        this.chatsView = chatsView;
        this.userChatsInteractor = new UserChatsInteractorImpl(this);
        this.chatsInteractor = new ChatsInteractorImpl(this);
    }

    @Override
    public void subscribeForChatListUpdates() {
        userChatsInteractor.subscribeForUserChatsUpdates();
    }

    @Override
    public void unsubscribeForChatListUpdates() {
        userChatsInteractor.unsubscribeForUserChatsUpdates();
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
