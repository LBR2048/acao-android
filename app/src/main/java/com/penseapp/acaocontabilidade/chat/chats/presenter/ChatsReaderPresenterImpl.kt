package com.penseapp.acaocontabilidade.chat.chats.presenter

import com.penseapp.acaocontabilidade.chat.chats.interactor.ChatsReaderInteractor
import com.penseapp.acaocontabilidade.chat.chats.interactor.ChatsReaderInteractorImpl
import com.penseapp.acaocontabilidade.chat.chats.model.Chat
import com.penseapp.acaocontabilidade.chat.chats.view.ChatsAdapterView

/**
 * Created by unity on 10/12/16.
 */

class ChatsReaderPresenterImpl(private val chatsView: ChatsAdapterView) : ChatsReaderPresenter {

    private val chatsReaderInteractor: ChatsReaderInteractor

    init {
        this.chatsReaderInteractor = ChatsReaderInteractorImpl(this)
    }

    override fun subscribeForChatListUpdates() {
        chatsReaderInteractor.subscribeForUserChatsUpdates()
    }

    override fun unsubscribeForChatListUpdates() {
        chatsReaderInteractor.unsubscribeForUserChatsUpdates()
    }

    override fun onChatAdded(chat: Chat) {
        chatsView.onChatAdded(chat)
    }

    override fun onChatChanged(chat: Chat) {
        chatsView.onChatChanged(chat)
    }

    override fun onChatRemoved(chatId: String) {
        chatsView.onChatRemoved(chatId)
    }
}
