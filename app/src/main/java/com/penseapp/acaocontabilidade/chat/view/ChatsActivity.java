package com.penseapp.acaocontabilidade.chat.view;

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
import com.penseapp.acaocontabilidade.chat.adapters.ChatListAdapter;
import com.penseapp.acaocontabilidade.chat.model.Chat;
import com.penseapp.acaocontabilidade.chat.presenter.ChatsPresenter;
import com.penseapp.acaocontabilidade.chat.presenter.ChatsPresenterImpl;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.login.view.activities.LoginActivity;

import java.util.ArrayList;

public class ChatsActivity extends AppCompatActivity implements ChatsView {

    private static final String LOG_TAG = ChatsActivity.class.getSimpleName();
    public static final String SELECTED_CHAT_KEY = "selected_chat_key";
    public static final String SELECTED_CHAT_NAME = "selected_chat_name";

    private ChatsPresenter chatsPresenter;

    public static ArrayList<Chat> mChats = new ArrayList<>();
    private ChatListAdapter chatListAdapter;
    private RecyclerView mChatsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        setupToolBar();

        // Set screen title
        String currentUserEmail = FirebaseHelper.getInstance().getAuthUserEmail();
        setTitle("Chats [" + currentUserEmail + " ]");

        // TODO
//        // Change button text depending on who you are
//        Button createChatButton = (Button) findViewById(R.id.button_create_chat);
//        // If current user is a customer
//        if (userType == "customer")
//            createChatButton.setText("Fazer novo chamado");
//        // If current user works at Ação
//        if (userType == "acao")
//            createChatButton.setText("Entrar em contato com cliente");

        // Chats list RecyclerView
        mChatsRecyclerView = (RecyclerView) findViewById(R.id.list_chats);
        setupRecyclerView();
//        setupRecyclerViewDecorator();
//        setOnItemDragDropSwipe();
        setOnItemClickListener();
//        setOnItemDismissListener();
//        setOnWorkoutShareClickListener();
//        setOnWorkoutRenameClickListener();
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

//        if (id == R.id.action_feedback) {
//            // Create an instance of the dialog fragment and show it
//            DialogFragment surveyDialogFragment = new SurveyDialogFragment();
//            surveyDialogFragment.show(getSupportFragmentManager(), "SurveyDialogFragment");
//        }

        return super.onOptionsItemSelected(item);
    }

    // Workouts list

    private void setupRecyclerView() {
        chatListAdapter = new ChatListAdapter(mChats);
        mChatsRecyclerView.setAdapter(chatListAdapter);
        mChatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

//    private void setupRecyclerViewDecorator() {
//        // Display dividers between each item of the RecyclerView
//        RecyclerView.ItemDecoration itemDecoration = new
//                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
//        mChatsRecyclerView.addItemDecoration(itemDecoration);
//    }

    private void setOnItemClickListener() {
        // What happens when a chat item is clicked
        chatListAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Chat selectedChat = mChats.get(position);
                Log.i(LOG_TAG, selectedChat.getName() + " clicked");

                // Send the selected chat key to MessagesActivity
                Intent intent = new Intent(ChatsActivity.this, MessagesActivity.class);
                intent.putExtra(SELECTED_CHAT_KEY, selectedChat.getKey());
                intent.putExtra(SELECTED_CHAT_NAME, selectedChat.getName());
                startActivity(intent);

                // Send the selected chat position to the ChatMessagesActivity
//                Intent intent = new Intent(ChatListActivity.this, ExercisesActivity.class);
//                intent.putExtra(SELECTED_WORKOUT_ID, position);

                // Firebase key
//                intent.putExtra(SELECTED_WORKOUT_KEY, selectedChat.getKey());
//                startActivityForResult(intent, ExercisesActivity.REQUEST_VIEW_EXERCISES);
            }
        });
    }

//    private void setOnItemDismissListener() {
//        // What happens when an item is dismissed
//        chatListAdapter.setOnItemDismissListener(new WorkoutsAdapter.OnItemDismissListener() {
//            @Override
//            public void onItemDismiss(int position) {
//                String key = chatListAdapter.getItem(position).getKey();
//                Log.i(LOG_TAG, "Removing item " + key + " from position " + Integer.toString(position));
//                workoutsPresenter.removeWorkout(key);
//            }
//        });
//    }

//    private void setOnItemDragDropSwipe() {
//        // Add drag & drop and swipe capabilities to the RecyclerView items
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(chatListAdapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(mWorkoutsRecyclerView);
//    }

    private void clearRecyclerView() {
        mChats.clear();
        chatListAdapter.notifyDataSetChanged(); // TODO not efficient
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to Presenters
        chatsPresenter = new ChatsPresenterImpl(this);

        clearRecyclerView();
        chatsPresenter.subscribeForChatListUpdates();
    }

    @Override
    protected void onStop() {
        chatsPresenter.unsubscribeForChatListUpdates();
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
//                chatsPresenter.createChatIfNeeded(chatName, contactId);
                chatsPresenter.createChat(chatName, contactId);
            }
        }
    }

    @Override
    public void onChatAdded(Chat chat) {
        Log.i(LOG_TAG, "View onChatAdded called");
        mChats.add(chat);
        chatListAdapter.notifyItemInserted(mChats.size() - 1);
    }

    @Override
    public void onChatChanged(Chat chat) {
        Log.i(LOG_TAG, "View onChatChanged called");
        int index = getIndexForKey(chat.getKey());
        mChats.set(index, chat);
        chatListAdapter.notifyItemChanged(index);
    }

    @Override
    public void onChatRemoved(String chatId) {
        Log.i(LOG_TAG, "View onChatRemoved called");
        try {
            int index = getIndexForKey(chatId);
            mChats.remove(index);
            chatListAdapter.notifyItemRemoved(index);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
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
