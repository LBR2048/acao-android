package com.penseapp.acaocontabilidade.chat.chats.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.penseapp.acaocontabilidade.R
import com.penseapp.acaocontabilidade.chat.chats.model.Chat
import kotlinx.android.synthetic.main.fragment_chats.*
import java.util.*

class ChatsFragment : Fragment() {
    private var mListener: OnChatsFragmentInteractionListener? = null
    private var chatsAdapter: ChatsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupRecyclerViewDecorator()
        clearRecyclerView()

        // What happens when a chat from the list is clicked
        chatsAdapter!!.setOnItemClickListener(object : ChatsAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int) {
                val item = mChats[position]
                Log.i(LOG_TAG, "$item.name clicked")
                onChatClicked(item.name, item.key)
            }
        })

        fab.setOnClickListener { mListener!!.onShowContactsClicked() }
    }

    private fun onChatClicked(name: String, key: String?) {
        if (mListener != null) {
            mListener!!.onChatSelected(key, name)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnChatsFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        chatsAdapter!!.unsubscribeForChatListUpdates()

        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnChatsFragmentInteractionListener {
        fun onChatSelected(key: String?, name: String)
        fun onShowContactsClicked()
    }

    private fun setupRecyclerView() {
        chatsAdapter = ChatsAdapter(mChats, context!!)
        chatsAdapter!!.subscribeForChatsUpdates()
        chatsRecyclerView!!.adapter = chatsAdapter
        chatsRecyclerView!!.layoutManager = LinearLayoutManager(activity)
    }

    private fun setupRecyclerViewDecorator() {
        // Display dividers between each item of the RecyclerView
        if (context != null) {
            val itemDecoration = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
            chatsRecyclerView!!.addItemDecoration(itemDecoration)
        }
    }

    private fun clearRecyclerView() {
        mChats.clear()
        chatsAdapter!!.notifyDataSetChanged()
    }

    companion object {

        private val LOG_TAG = ChatsFragment::class.java.simpleName

        private val mChats = ArrayList<Chat>()

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ChatsFragment.
         * @param context
         */
        fun newInstance(context: Context): ChatsFragment {
            val fragment = ChatsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
