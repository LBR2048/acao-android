package com.penseapp.acaocontabilidade.chat.chats.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.chats.model.Chat;
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsReaderPresenter;
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsReaderPresenterImpl;

import java.util.List;


/**
 * Created by unity on 27/10/15.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder>
        implements ChatsAdapterView { //ItemTouchHelperAdapter {

    private static final String LOG_TAG = ChatsAdapter.class.getSimpleName();

    // Define onItemClickListener member variable
    private static OnItemClickListener onItemClickListener;

    private final List<Chat> mChats;
    private final Context mContext;
    private final ChatsReaderPresenter chatsReaderPresenter;

    public ChatsAdapter(List<Chat> chats, Context context) {
        mChats = chats;
        mContext = context;
        chatsReaderPresenter = new ChatsReaderPresenterImpl(this);
    }


    // Define the onItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Allows the parent activity or fragment to define the onItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView name;
        TextView unreadMessageCount;
        FrameLayout badge;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.list_item_chat_name_textview);
            unreadMessageCount = (TextView) itemView.findViewById(R.id.list_item_chat_unread_messages_textview);
            badge = (FrameLayout) itemView.findViewById(R.id.list_item_chat_unread_messages_badge);

            // Setup the click onItemClickListener
            // itemView.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);

        // Return a new holder instance
        return new ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get the data model based on position
        final Chat selectedChat = mChats.get(position);

        // Set item views based on your views and data model
        String recipientName = selectedChat.getName();
//        String senderName = selectedChat.getFirstUserName();

//        SharedPreferences mSettings = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE);
//        String currentUserName = mSettings.getString("userName", "");
//        if (senderName.equals(currentUserName)) {
//            holder.name.setText(recipientName);
//        } else {
//            holder.name.setText(senderName);
//        }
        holder.name.setText(recipientName);

        int unreadMessageCount = selectedChat.getUnreadMessageCount();
        if (unreadMessageCount != 0) {
            holder.unreadMessageCount.setText(Integer.toString(unreadMessageCount));
            holder.badge.setVisibility(View.VISIBLE);
        } else {
            holder.badge.setVisibility(View.GONE);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public Chat getItem(int position) {
        return mChats.get(position);
    }

    @Override
    public void subscribeForChatsUpdates() {
        chatsReaderPresenter.subscribeForChatListUpdates();
    }

    @Override
    public void unsubscribeForChatListUpdates() {
        chatsReaderPresenter.unsubscribeForChatListUpdates();
    }

    @Override
    public void onChatAdded(Chat chat) {
        Log.i(LOG_TAG, "View onChatAdded called");
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    @Override
    public void onChatChanged(Chat chat) {
        Log.i(LOG_TAG, "View onChatChanged called");
        int index = getIndexForKey(chat.getKey());
        mChats.set(index, chat);
        notifyItemChanged(index);
    }

    @Override
    public void onChatRemoved(String chatId) {
        Log.i(LOG_TAG, "View onChatRemoved called");
        try {
            int index = getIndexForKey(chatId);
            mChats.remove(index);
            notifyItemRemoved(index);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // TODO this method should go somewhere else? Does it belong in the View?
    // TODO duplicado em ExerciseChooserActivity
    private int getIndexForKey(String key) {
        int index = 0;
        for (Chat chat : mChats) {
            if (chat.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }
}