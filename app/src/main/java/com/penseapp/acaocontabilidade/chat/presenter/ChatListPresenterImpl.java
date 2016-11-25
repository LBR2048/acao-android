package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.interactor.ChatListInteractor;
import com.penseapp.acaocontabilidade.chat.interactor.ChatListInteractorImpl;
import com.penseapp.acaocontabilidade.chat.model.Chat;
import com.penseapp.acaocontabilidade.chat.view.ChatsView;

/**
 * Created by unity on 21/11/16.
 */

public class ChatListPresenterImpl implements ChatListPresenter {

    private final ChatsView chatsView;
    private final ChatListInteractor chatListInteractor;

    public ChatListPresenterImpl(ChatsView chatsView) {
        this.chatsView = chatsView;
        this.chatListInteractor = new ChatListInteractorImpl(this);
    }

    @Override
    public void subscribeForChatListUpdates() {
        chatListInteractor.subscribeForChatListUpdates();
    }

    @Override
    public void unsubscribeForChatListUpdates() {
        chatListInteractor.unsubscribeForChatListUpdates();
    }

    @Override
    public void createChat(String chatName, String contactId) {
        chatListInteractor.createChat(chatName, contactId);
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
