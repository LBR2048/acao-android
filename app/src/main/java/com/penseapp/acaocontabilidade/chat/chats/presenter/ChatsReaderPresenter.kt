package com.penseapp.acaocontabilidade.chat.chats.presenter

import com.penseapp.acaocontabilidade.chat.chats.model.Chat

/**
 * Created by unity on 10/12/16.
 */

interface ChatsReaderPresenter {

    fun subscribeForChatListUpdates()

    fun unsubscribeForChatListUpdates()

    fun onChatAdded(chat: Chat)

    fun onChatChanged(chat: Chat)

    fun onChatRemoved(chatId: String)
}
