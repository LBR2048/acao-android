package com.penseapp.acaocontabilidade.chat.chats.view

import com.penseapp.acaocontabilidade.chat.chats.model.Chat

/**
 * Created by unity on 03/02/17.
 */

interface ChatsAdapterView {

    fun subscribeForChatsUpdates()

    fun unsubscribeForChatListUpdates()

    fun onChatAdded(chat: Chat)

    fun onChatChanged(chat: Chat)

    fun onChatRemoved(chatId: String)
}
