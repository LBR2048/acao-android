package com.penseapp.acaocontabilidade.chat.messages.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.messages.model.Message;
import com.penseapp.acaocontabilidade.chat.messages.presenter.MessagesPresenter;
import com.penseapp.acaocontabilidade.chat.messages.presenter.MessagesPresenterImpl;
import com.penseapp.acaocontabilidade.chat.chats.view.ChatsActivity;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity implements MessagesView {
    private final static String LOG_TAG = MessagesActivity.class.getSimpleName();
    public static ArrayList<Message> mMessages = new ArrayList<>();
    private MessagesPresenter messagesPresenter;
    private RecyclerView mMessagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    private String mChatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setupToolBar();

        // Get information from Intent that called this Activity
        mChatId = getIntent().getExtras().getString(ChatsActivity.SELECTED_CHAT_KEY);
        String mChatName = getIntent().getExtras().getString(ChatsActivity.SELECTED_CHAT_NAME);
        setTitle(mChatName);

        mMessagesRecyclerView = (RecyclerView) findViewById(R.id.list_messages);

        // Chats list RecyclerView
        setupRecyclerView();
//        setupRecyclerViewDecorator();
//        setOnItemDragDropSwipe();
//        setOnItemClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to Presenters
        messagesPresenter = new MessagesPresenterImpl(this, mChatId);

        clearRecyclerView();
        resetUnreadMessageCount();
        messagesPresenter.subscribeForMessagesUpdates();
    }

    @Override
    protected void onStop() {
        messagesPresenter.unsubscribeForMessagesUpdates();
        super.onStop();
    }

    // Toolbar
    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    // Messages list

    private void setupRecyclerView() {
        messagesAdapter = new MessagesAdapter(mMessages);
        mMessagesRecyclerView.setAdapter(messagesAdapter);
        mMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mMessagesRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
//                mMessagesRecyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mMessagesRecyclerView.smoothScrollToPosition(messagesAdapter.getItemCount() - 1);
//                    }
//                }, 100);
//                mMessagesRecyclerView.smoothScrollToPosition(mMessages.size() - 1);
//                Toast.makeText(getApplicationContext(), "Evento", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void clearRecyclerView() {
        mMessages.clear();
        messagesAdapter.notifyDataSetChanged(); // TODO not efficient
    }

    public void resetUnreadMessageCount() {
        messagesPresenter.resetUnreadMessageCount(mChatId);
    }

    @Override
    public void onMessageAdded(Message message) {
        Log.i(LOG_TAG, "View onMessageAdded called");
        mMessages.add(message);
        messagesAdapter.notifyItemInserted(mMessages.size() - 1);
        mMessagesRecyclerView.smoothScrollToPosition(mMessages.size() - 1);

        resetUnreadMessageCount();
    }

    public void onClickSendMessage(View view) {
//        Toast.makeText(getApplicationContext(), "Enviando mensagem", Toast.LENGTH_SHORT).show();
        EditText messageEdit = (EditText) findViewById(R.id.messageEdit);
        Button sendButton = (Button) findViewById(R.id.sendButton);

        // Create message with entered text and current user information
        String text = messageEdit.getText().toString().trim();
        String senderId = FirebaseHelper.getInstance().getAuthUserId();
        String senderName = FirebaseHelper.getInstance().getAuthUserEmail();

        // Clear messageEdit and hide keyboard
        messageEdit.getText().clear();
//        Utilities.hideSoftKeyboard(this);

        if (!text.isEmpty()) {
            messagesPresenter.sendMessage(text, senderId, senderName);
        }
    }
}