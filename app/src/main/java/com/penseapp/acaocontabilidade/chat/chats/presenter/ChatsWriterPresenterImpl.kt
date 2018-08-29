package com.penseapp.acaocontabilidade.chat.chats.presenter

import com.penseapp.acaocontabilidade.chat.chats.interactor.ChatsWriterInteractor
import com.penseapp.acaocontabilidade.chat.chats.interactor.ChatsWriterInteractorImpl
import com.penseapp.acaocontabilidade.chat.contacts.view.ContactsView

/**
 * Created by unity on 21/11/16.
 */

class ChatsWriterPresenterImpl(private val contactsView: ContactsView) : ChatsWriterPresenter {

    private val chatsWriterInteractor: ChatsWriterInteractor

    init {
        this.chatsWriterInteractor = ChatsWriterInteractorImpl(this)
    }

    override fun createChatIfNeeded(senderId: String,
                                    senderName: String,
                                    senderCompany: String,
                                    recipientId: String,
                                    recipientName: String,
                                    recipientCompany: String) {

        chatsWriterInteractor.createChatIfNeeded(senderId, senderName, senderCompany, recipientId,
                recipientName, recipientCompany)
    }

    override fun onChatCreated(chatId: String, chatName: String) {
        contactsView.onChatCreated(chatId, chatName)
    }
}
