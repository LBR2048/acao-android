package com.penseapp.acaocontabilidade.chat.chats.interactor

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.penseapp.acaocontabilidade.chat.chats.model.Chat
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsWriterPresenter
import com.penseapp.acaocontabilidade.domain.FirebaseHelper

/**
 * Created by Cleusa on 27/11/2016.
 */

class ChatsWriterInteractorImpl(private val chatsWriterPresenter: ChatsWriterPresenter) : ChatsWriterInteractor {

    private val mFirebaseHelperInstance = FirebaseHelper.getInstance()
    private val userChatContactsRef = mFirebaseHelperInstance.userChatContactsReference

    private val authUserId = mFirebaseHelperInstance.authUserId

    override fun createChatIfNeeded(senderId: String,
                                    senderName: String,
                                    senderCompany: String,
                                    recipientId: String,
                                    recipientName: String,
                                    recipientCompany: String) {

        if (authUserId != null) {
            userChatContactsRef.child(authUserId).child(recipientId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                val chatKey = createChat(senderId, senderName, senderCompany,
                                        recipientId, recipientName, recipientCompany)
                                if (chatKey != null) {
                                    chatsWriterPresenter.onChatCreated(chatKey, recipientName)
                                }
                            } else {
                                chatsWriterPresenter.onChatCreated(dataSnapshot.value.toString(),
                                        recipientName)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }
                    })
        }
    }

    private fun createChat(senderId: String,
                           senderName: String,
                           senderCompany: String,
                           recipientId: String,
                           recipientName: String,
                           recipientCompany: String): String? {

        val userChatPropertiesReference = mFirebaseHelperInstance.userChatPropertiesReference

        // Create a key for the new chat
        val chatKey = userChatPropertiesReference.child(senderId).push().key

        if (chatKey != null) {
            // Create a recipient chat using the key
            val recipientChat = Chat(contactId = recipientId, contactCompany = recipientCompany, name = recipientName)
            userChatPropertiesReference.child(senderId).child(chatKey).setValue(recipientChat)

            // Create a sender chat using the same key
            val senderChat = Chat(contactId = senderId, contactCompany = senderCompany, name = senderName)
            userChatPropertiesReference.child(recipientId).child(chatKey).setValue(senderChat)

            // Add reference to newly created chat to user-chatContacts-chat/$authUserId/$contactId:$newChatId
            userChatContactsRef.child(authUserId).child(recipientId).setValue(chatKey)

            // Add reference to newly created chat to user-chatContacts-chat/$contactId/$authUserId:$newChatId
            userChatContactsRef.child(recipientId).child(authUserId).setValue(chatKey)
        }

        return chatKey

        //region Creio que é útil apenas para grupos. Retirar por enquanto.
        // For each $userId:
        // Create chat-users/$userId
        //        mFirebaseHelperInstance.getChatUsersReference().child(newChatKey).child(senderId).setValue(true);
        //        mFirebaseHelperInstance.getChatUsersReference().child(newChatKey).child(recipientId).setValue(true);

        // For each $userId:
        // Create chat at user-chats-properties/$userId/$chatId
        //        mFirebaseHelperInstance.getUserChatPropertiesReference().child(senderId).child(newChatKey).setValue(newChat);
        //        mFirebaseHelperInstance.getUserChatPropertiesReference().child(recipientId).child(newChatKey).setValue(newChat);

        // For each $userId:
        // Create user-chats-properties/users/$userId
        //        mFirebaseHelperInstance.getUserChatPropertiesReference().child(senderId).child(newChatKey).child("users").child(senderId).setValue(true);
        //        mFirebaseHelperInstance.getUserChatPropertiesReference().child(senderId).child(newChatKey).child("users").child(recipientId).setValue(true);
        //        mFirebaseHelperInstance.getUserChatPropertiesReference().child(recipientId).child(newChatKey).child("users").child(senderId).setValue(true);
        //        mFirebaseHelperInstance.getUserChatPropertiesReference().child(recipientId).child(newChatKey).child("users").child(recipientId).setValue(true);
        //endregion

    }
}
