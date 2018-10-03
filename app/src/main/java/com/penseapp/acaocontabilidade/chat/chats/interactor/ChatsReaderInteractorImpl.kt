package com.penseapp.acaocontabilidade.chat.chats.interactor

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.penseapp.acaocontabilidade.chat.chats.model.Chat
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsReaderPresenter
import com.penseapp.acaocontabilidade.domain.FirebaseHelper

/**
 * Created by unity on 21/11/16.
 */

class ChatsReaderInteractorImpl

(private val chatsReaderPresenter: ChatsReaderPresenter) : ChatsReaderInteractor {
    private var chatsChildEventListener: ChildEventListener? = null

    //region Firebase
    private val mFirebaseHelperInstance = FirebaseHelper.getInstance()
    private val currentUserChatsReference = mFirebaseHelperInstance.currentUserChatsReference
    private val currentUserId = mFirebaseHelperInstance.authUserId

    override fun subscribeForUserChatsUpdates() {
        if (chatsChildEventListener == null) {
            Log.i(LOG_TAG, "subscribeForUserChatsUpdates called")

            chatsChildEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        chat.key = dataSnapshot.key
                        chatsReaderPresenter.onChatAdded(chat)
                        Log.i(LOG_TAG, "Chat added ${chat.name}")
                    } else {
                        Log.e(LOG_TAG, "Error reading chat " + dataSnapshot.key!!)
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        chat.key = dataSnapshot.key
                        chatsReaderPresenter.onChatChanged(chat)
                        Log.i(LOG_TAG, "Chat changed ${chat.name}")
                    } else {
                        Log.e(LOG_TAG, "Error while reading chat " + dataSnapshot.key!!)
                    }

                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val key = dataSnapshot.key
                    if (key != null) {
                        Log.i(LOG_TAG, "Chat removed $key")
                        chatsReaderPresenter.onChatRemoved(key)
                    } else {
                        Log.e(LOG_TAG, "Error removing chat")
                    }
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }

            val userChatPropertiesReference = mFirebaseHelperInstance.userChatPropertiesReference
            userChatPropertiesReference.child(currentUserId!!).orderByChild("latestMessageTimestamp").addChildEventListener(chatsChildEventListener!!)
        }
    }

    override fun unsubscribeForUserChatsUpdates() {
        if (chatsChildEventListener != null) {
            Log.i(LOG_TAG, "unsubscribeForUserChatsUpdates called")

            currentUserChatsReference.removeEventListener(chatsChildEventListener!!)
        }
    }

    companion object {

        private val LOG_TAG = ChatsReaderInteractorImpl::class.java.simpleName
    }

}