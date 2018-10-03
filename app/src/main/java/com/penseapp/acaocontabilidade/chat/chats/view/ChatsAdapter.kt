package com.penseapp.acaocontabilidade.chat.chats.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.penseapp.acaocontabilidade.R
import com.penseapp.acaocontabilidade.chat.chats.model.Chat
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsReaderPresenter
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsReaderPresenterImpl


/**
 * Created by unity on 27/10/15.
 */

class ChatsAdapter(
        private val mChats: MutableList<Chat>,
        private val mContext: Context)
    : RecyclerView.Adapter<ChatsAdapter.ViewHolder>(), ChatsAdapterView {

    private val chatsReaderPresenter: ChatsReaderPresenter

    init {
        chatsReaderPresenter = ChatsReaderPresenterImpl(this)
    }

    // Define the onItemClickListener interface
    interface OnItemClickListener {
        fun onItemClick(itemView: View, position: Int)
    }

    // Allows the parent activity or fragment to define the onItemClickListener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_chat_contact, parent, false)
        return ViewHolder(view)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mChats[position]

        holder.name.text = item.name

        when (item.name) {
            CONTABIL -> holder.icon.setImageResource(R.drawable.ic_contabil)
            FISCAL -> holder.icon.setImageResource(R.drawable.ic_fiscal)
            PESSOAL -> holder.icon.setImageResource(R.drawable.ic_pessoal)
            SOCIETARIO -> holder.icon.setImageResource(R.drawable.ic_societario)
            else -> holder.icon.setImageResource(R.drawable.ic_default)
        }

        holder.availability.setText(R.string.status_available)

        if (item.unreadMessageCount != 0) {
            holder.unreadMessageCount.text = Integer.toString(item.unreadMessageCount)
            holder.badge.visibility = View.VISIBLE
        } else {
            holder.badge.visibility = View.GONE
        }
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val icon: ImageView = itemView.findViewById(R.id.list_item_chat_contact_icon)
        internal val name: TextView = itemView.findViewById(R.id.list_item_chat_contact_name_textview)
        internal val unreadMessageCount: TextView = itemView.findViewById(R.id.list_item_chat_contact_unread_messages_textview)
        internal val badge: FrameLayout = itemView.findViewById(R.id.list_item_chat_contact_unread_messages_badge)
        internal val availability: TextView = itemView.findViewById(R.id.list_item_chat_contact_availability_textview)

        init {
            itemView.setOnClickListener {
                // Triggers click upwards to the adapter on click
                if (onItemClickListener != null)
                    onItemClickListener!!.onItemClick(itemView, layoutPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return mChats.size
    }

    fun getItem(position: Int): Chat {
        return mChats[position]
    }

    override fun subscribeForChatsUpdates() {
        chatsReaderPresenter.subscribeForChatListUpdates()
    }

    override fun unsubscribeForChatListUpdates() {
        chatsReaderPresenter.unsubscribeForChatListUpdates()
    }

    override fun onChatAdded(chat: Chat) {
        Log.i(LOG_TAG, "View onChatAdded called")
        mChats.add(chat)
        notifyItemInserted(mChats.size - 1)
    }

    override fun onChatChanged(chat: Chat) {
        Log.i(LOG_TAG, "View onChatChanged called")
        val index = getIndexForKey(chat.key)
        mChats[index] = chat
        notifyItemChanged(index)
    }

    override fun onChatRemoved(chatId: String) {
        Log.i(LOG_TAG, "View onChatRemoved called")
        try {
            val index = getIndexForKey(chatId)
            mChats.removeAt(index)
            notifyItemRemoved(index)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    // TODO this method should go somewhere else? Does it belong in the View?
    // TODO duplicado em ExerciseChooserActivity
    private fun getIndexForKey(key: String?): Int {
        var index = 0
        for (chat in mChats) {
            if (chat.key == key) {
                return index
            } else {
                index++
            }
        }
        throw IllegalArgumentException("Key not found")
    }

    companion object {

        //region Constants
        private val LOG_TAG = ChatsAdapter::class.java.simpleName
        private const val CONTABIL = "Contábil"
        private const val FISCAL = "Fiscal"
        private const val PESSOAL = "Pessoal"
        private const val SOCIETARIO = "Societário"
        //endregion

        private var onItemClickListener: OnItemClickListener? = null
    }
}