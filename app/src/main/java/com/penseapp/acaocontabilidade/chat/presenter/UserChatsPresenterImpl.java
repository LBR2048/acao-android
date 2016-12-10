package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.interactor.UserChatsInteractor;
import com.penseapp.acaocontabilidade.chat.interactor.UserChatsInteractorImpl;
import com.penseapp.acaocontabilidade.chat.model.Chat;
import com.penseapp.acaocontabilidade.chat.view.ChatsView;

/**
 * Created by unity on 10/12/16.
 */

public class UserChatsPresenterImpl implements UserChatsPresenter {

    private final ChatsView chatsView;
    private final UserChatsInteractor userChatsInteractor;

    public UserChatsPresenterImpl(ChatsView chatsView) {
        this.chatsView = chatsView;
        this.userChatsInteractor = new UserChatsInteractorImpl(this);
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
