package com.penseapp.acaocontabilidade.chat.chats.model

class Chat (
        var key: String? = null,
        var name: String = "",
        var contactId: String? = null,
        var contactCompany: String? = null,
        var unreadMessageCount: Int = 0,
        var latestMessageTimestamp: Long = 0
) {
    override fun toString(): String {
        return name
    }
}
