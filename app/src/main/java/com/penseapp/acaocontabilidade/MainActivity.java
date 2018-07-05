package com.penseapp.acaocontabilidade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.penseapp.acaocontabilidade.chat.chats.view.ChatsFragment;
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.domain.Utilities;
import com.penseapp.acaocontabilidade.login.view.activities.LoginActivity;

import static com.penseapp.acaocontabilidade.chat.chats.view.ChatsActivity.SELECTED_CHAT_KEY;
import static com.penseapp.acaocontabilidade.chat.chats.view.ChatsActivity.SELECTED_CHAT_NAME;

public class MainActivity extends AppCompatActivity implements
        ChatsFragment.OnChatsFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolBar();

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_holder, ChatsFragment.newInstance(this))
                    .commit();
        }
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

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
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

}
