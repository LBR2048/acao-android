package com.penseapp.acaocontabilidade.chat.chats.interactor

/**
 * Created by Cleusa on 27/11/2016.
 */

interface ChatsWriterInteractor {

    fun createChatIfNeeded(senderId: String,
                           senderName: String,
                           senderCompany: String,
                           recipientId: String,
                           recipientName: String,
                           recipientCompany: String)
}
