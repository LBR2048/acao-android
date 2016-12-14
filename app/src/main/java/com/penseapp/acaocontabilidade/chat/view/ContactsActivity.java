package com.penseapp.acaocontabilidade.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.adapters.ContactsAdapter;
import com.penseapp.acaocontabilidade.chat.presenter.ChatsPresenter;
import com.penseapp.acaocontabilidade.chat.presenter.ChatsPresenterImpl;
import com.penseapp.acaocontabilidade.chat.presenter.ContactsPresenter;
import com.penseapp.acaocontabilidade.chat.presenter.ContactsPresenterImpl;
import com.penseapp.acaocontabilidade.login.model.User;

import java.util.ArrayList;

import static com.penseapp.acaocontabilidade.chat.view.ChatsActivity.SELECTED_CHAT_KEY;
import static com.penseapp.acaocontabilidade.chat.view.ChatsActivity.SELECTED_CHAT_NAME;

public class ContactsActivity extends AppCompatActivity implements ContactsView{

    private final static String LOG_TAG = ContactsActivity.class.getSimpleName();
    public static final int CONTACT_REQUEST = 0;
    public static final String SELECTED_CONTACT_NAME = "selected_contact";
    public static final String SELECTED_CONTACT_KEY = "selected_contact_key";
    private ContactsPresenter contactsPresenter;
    private ChatsPresenter chatsPresenter;

    public static ArrayList<User> mContacts = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private RecyclerView mContactsRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setupToolBar();
        setTitle("Abrir chamado");
        mContactsRecyclerView = (RecyclerView) findViewById(R.id.list_contacts);

        // Chats list RecyclerView
        setupRecyclerView();
//        setupRecyclerViewDecorator();
//        setOnItemDragDropSwipe();
        setOnItemClickListener();
//        setOnItemDismissListener();
//        setOnWorkoutShareClickListener();
//        setOnWorkoutRenameClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to Presenters
        contactsPresenter = new ContactsPresenterImpl(this);
        chatsPresenter = new ChatsPresenterImpl(this);

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

//                Intent data = new Intent();
//                // Pass relevant data back as a result
//                data.putExtra("name", etName.getText().toString());
//                data.putExtra("code", 200); // ints work too
//                // Activity finished ok, return the data
                chatsPresenter.createChatIfNeeded(selectedContact.getName(), selectedContact.getKey());

//                Intent data = new Intent();
//                data.putExtra(SELECTED_CONTACT_KEY, selectedContact.getKey());
//                data.putExtra(SELECTED_CONTACT_NAME, selectedContact.getName());
//
//                setResult(RESULT_OK, data); // set result code and bundle data for response
//                finish(); // closes the activity, pass data to parent
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


//    @Override
//    public void onContactAdded(User contact) {
//        Log.i(LOG_TAG, "View onContactAdded called");
//        mContacts.add(contact);
//        contactsAdapter.notifyItemInserted(mContacts.size() - 1);
//    }
//
//    @Override
//    public void onContactChanged(User contact) {
//        Log.i(LOG_TAG, "View onContactChanged called");
//        int index = getIndexForKey(contact.getKey());
//        mContacts.set(index, contact);
//        contactsAdapter.notifyItemChanged(index);
//    }
//
//    @Override
//    public void onContactRemoved(String contactId) {
//        Log.i(LOG_TAG, "View onContactRemoved called");
//        try {
//            int index = getIndexForKey(contactId);
//            mChats.remove(index);
//            contactsAdapter.notifyItemRemoved(index);
//        } catch(IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // TODO this method should go somewhere else? Does it belong in the View?
//    // TODO duplicado em ExerciseChooserActivity
//    private int getIndexForKey(String key) {
//        int index = 0;
//        for (User contact : mContacts) {
//            if (contact.getKey().equals(key)) {
//                return index;
//            } else {
//                index++;
//            }
//        }
//        throw new IllegalArgumentException("Key not found");
//    }
}
