package com.penseapp.acaocontabilidade.chat.chats.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity;
import com.penseapp.acaocontabilidade.chat.chats.model.Chat;
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsWriterPresenter;
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsReaderPresenter;
import com.penseapp.acaocontabilidade.chat.contacts.view.ContactsActivity;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.login.view.activities.LoginActivity;

import java.util.ArrayList;

public class ChatsActivity extends AppCompatActivity implements ChatsView {

    private static final String LOG_TAG = ChatsActivity.class.getSimpleName();
    public static final String SELECTED_CHAT_KEY = "selected_chat_key";
    public static final String SELECTED_CHAT_NAME = "selected_chat_name";

    private ChatsReaderPresenter chatsReaderPresenter;
    private ChatsWriterPresenter chatsWriterPresenter;


    public static ArrayList<Chat> mChats = new ArrayList<>();
    private ChatsAdapter chatsAdapter;
    private RecyclerView mChatsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        setupToolBar();

        // Set screen title
        String currentUserEmail = FirebaseHelper.getInstance().getAuthUserEmail();
        setTitle("Chats [" + currentUserEmail + " ]");

        // Chats list RecyclerView
        mChatsRecyclerView = (RecyclerView) findViewById(R.id.list_chats);
        setupRecyclerView();
        setOnItemClickListener();

    }

    // Toolbar
    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    // Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            showLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        chatsAdapter = new ChatsAdapter(mChats, getApplicationContext());
        mChatsRecyclerView.setAdapter(chatsAdapter);
        mChatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setOnItemClickListener() {
        // What happens when a chat item is clicked
        chatsAdapter.setOnItemClickListener(new ChatsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Chat selectedChat = mChats.get(position);
                Log.i(LOG_TAG, selectedChat.getName() + " clicked");

                // Send the selected chat key to MessagesActivity
                Intent intent = new Intent(ChatsActivity.this, MessagesActivity.class);
                intent.putExtra(SELECTED_CHAT_KEY, selectedChat.getKey());
                intent.putExtra(SELECTED_CHAT_NAME, selectedChat.getName());
                startActivity(intent);

            }
        });
    }

    private void clearRecyclerView() {
        mChats.clear();
        chatsAdapter.notifyDataSetChanged(); // TODO not efficient
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to Presenters

        clearRecyclerView();
    }

    @Override
    protected void onStop() {
        chatsReaderPresenter.unsubscribeForChatListUpdates();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ContactsActivity.CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                String chatName = data.getExtras().getString(ContactsActivity.SELECTED_CONTACT_NAME);
                String contactId = data.getExtras().getString(ContactsActivity.SELECTED_CONTACT_KEY);

                // Create new chat only if there isn't already a chat between currentUserId and contactId
//                chatsPresenter.createChatIfNeeded(, , contactId, chatName);
//                userChatsPresenter.createChat(chatName, contactId);
            }
        }
    }



    public void onClickCreateChat(View v) {
        Toast.makeText(getApplicationContext(), "Fazer novo chamado", Toast.LENGTH_SHORT).show();
        showContactsActivity();
    }

    // Navigation
    private void showContactsActivity() {
        Intent intent = new Intent(ChatsActivity.this, ContactsActivity.class);
        startActivityForResult(intent, ContactsActivity.CONTACT_REQUEST);
    }


    private void showLoginActivity() {
        Intent intent = new Intent(ChatsActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // TODO this method should go somewhere else? Does it belong in the View?
    // TODO duplicado em ExerciseChooserActivity
    private int getIndexForKey(String key) {
        int index = 0;
        for (Chat chat: mChats) {
            if (chat.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }

    // TODO isso não deveria pertencer a essa atividade, pois não diz respeito ao View
    private void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
    }
}
