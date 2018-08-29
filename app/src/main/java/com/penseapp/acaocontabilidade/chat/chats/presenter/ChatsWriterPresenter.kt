package com.penseapp.acaocontabilidade.chat.chats.presenter

/**
 * Created by unity on 21/11/16.
 */

interface ChatsWriterPresenter {

    fun createChatIfNeeded(senderId: String,
                           senderName: String,
                           senderCompany: String,
                           recipientId: String,
                           recipientName: String,
                           recipientCompany: String)

    fun onChatCreated(chatId: String, chatName: String)
}
