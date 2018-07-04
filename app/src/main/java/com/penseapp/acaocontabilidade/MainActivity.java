package com.penseapp.acaocontabilidade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsWriterPresenterImpl;
import com.penseapp.acaocontabilidade.chat.chats.view.ChatsFragment;
import com.penseapp.acaocontabilidade.chat.contacts.view.ContactsFragment;
import com.penseapp.acaocontabilidade.chat.contacts.view.ContactsView;
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.domain.Utilities;
import com.penseapp.acaocontabilidade.login.view.activities.LoginActivity;

import static com.penseapp.acaocontabilidade.chat.chats.view.ChatsActivity.SELECTED_CHAT_KEY;
import static com.penseapp.acaocontabilidade.chat.chats.view.ChatsActivity.SELECTED_CHAT_NAME;

public class MainActivity extends AppCompatActivity implements
        ContactsView,
        ChatsFragment.OnChatsFragmentInteractionListener,
        ContactsFragment.OnContactsFragmentInteractionListener {

    private ChatsWriterPresenterImpl chatsWriterPresenter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolBar();

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_holder, ChatsFragment.newInstance())
//                    .replace(R.id.main_activity_fragment_holder, ContactsFragment.newInstance())
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_fragment_holder);
                if (fragment instanceof ContactsFragment) {
                    toolbar.setTitle("Contatos");
                } else if (fragment instanceof ChatsFragment) {
                    toolbar.setTitle("Conversas");
//                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
                } else {
                    toolbar.setTitle("Ação");
                }
            }
        });

        chatsWriterPresenter = new ChatsWriterPresenterImpl(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_acao_web) {
            Utilities.goToAcaoWebsite(this);
            return true;
        }
        if (id == R.id.action_acao_facebook) {
            Utilities.goToAcaoFacebookWebsite(this);
            return true;
        }
        if (id == R.id.action_questor_documents) {
            Utilities.goToDocumentsWebsite(this);
            return true;
        }
        if (id == R.id.action_logout) {
            FirebaseHelper.getInstance().logout();
            navigateToLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChatSelected(String key, String name) {
        navigateToMessagesActivity(key, name);
    }

    @Override
    public void onShowContactsClicked() {
        showContactsFragment();
    }

    @Override
    public void onContactSelected(String key, String name, String company) {
        createChatIfNeeded(key, name, company);
    }

    @Override
    public void onChatCreated(String chatId, String chatName) {
        navigateToMessagesActivity(chatId, chatName);
    }

    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        toolbar.setTitle("Conversas");
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    private void navigateToMessagesActivity(String chatId, String chatName) {
        Intent intent = new Intent(this, MessagesActivity.class);
        intent.putExtra(SELECTED_CHAT_KEY, chatId);
        intent.putExtra(SELECTED_CHAT_NAME, chatName);
        startActivity(intent);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void showContactsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_activity_fragment_holder, ContactsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void createChatIfNeeded(String contactId, String contactName, String company) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String senderId = settings.getString("userId", "");
        String senderName = settings.getString("userName", "");
        String senderCompany = settings.getString("userCompany", "");
        chatsWriterPresenter.createChatIfNeeded(senderId, senderName, senderCompany, contactId, contactName, company);
    }

}
