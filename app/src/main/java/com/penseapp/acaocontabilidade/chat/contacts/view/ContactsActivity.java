package com.penseapp.acaocontabilidade.chat.contacts.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsWriterPresenter;
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsWriterPresenterImpl;
import com.penseapp.acaocontabilidade.chat.contacts.presenter.ContactsPresenter;
import com.penseapp.acaocontabilidade.chat.contacts.presenter.ContactsPresenterImpl;
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity;
import com.penseapp.acaocontabilidade.login.model.User;

import java.util.ArrayList;

import static com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity.SELECTED_CHAT_KEY;
import static com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity.SELECTED_CHAT_NAME;

public class ContactsActivity extends AppCompatActivity implements ContactsView{

    private final static String LOG_TAG = ContactsActivity.class.getSimpleName();
    public static final int CONTACT_REQUEST = 0;
    public static final String SELECTED_CONTACT_NAME = "selected_contact";
    public static final String SELECTED_CONTACT_KEY = "selected_contact_key";
    private ContactsPresenter contactsPresenter;
    private ChatsWriterPresenter chatsWriterPresenter;

    private static final ArrayList<User> mContacts = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private RecyclerView mContactsRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setupToolBar();
        setTitle("Abrir chamado");
        mContactsRecyclerView = findViewById(R.id.list_contacts);

        // Chats list RecyclerView
        setupRecyclerView();
        setOnItemClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to Presenters
        contactsPresenter = new ContactsPresenterImpl(this);
        chatsWriterPresenter = new ChatsWriterPresenterImpl(this);

        clearRecyclerView();
        contactsPresenter.subscribeForContactsUpdates();
    }

    @Override
    protected void onStop() {
        contactsPresenter.unsubscribeForContactsUpdates();
        super.onStop();
    }

    // Toolbar
    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    // Contacts list

    private void setupRecyclerView() {
        contactsAdapter = new ContactsAdapter(mContacts);
        mContactsRecyclerView.setAdapter(contactsAdapter);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setOnItemClickListener() {
        // What happens when a contact from the list is clicked
        contactsAdapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                User selectedContact = mContacts.get(position);
                Log.i(LOG_TAG, selectedContact.getName() + " clicked");
//                chatsPresenter.createChatIfNeeded(, , selectedContact.getKey(), selectedContact.getName());
            }
        });
    }

    @Override
    public void onChatCreated(String chatId, String chatName) {
        Intent intent = new Intent(ContactsActivity.this, MessagesActivity.class);
        intent.putExtra(SELECTED_CHAT_KEY, chatId);
        intent.putExtra(SELECTED_CHAT_NAME, chatName);
        startActivity(intent);
    }

    private void clearRecyclerView() {
        mContacts.clear();
        contactsAdapter.notifyDataSetChanged(); // TODO not efficient
    }

}
