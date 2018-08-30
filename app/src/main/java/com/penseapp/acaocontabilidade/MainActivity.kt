package com.penseapp.acaocontabilidade

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsWriterPresenterImpl
import com.penseapp.acaocontabilidade.chat.chats.view.ChatsFragment
import com.penseapp.acaocontabilidade.chat.contacts.view.ContactsFragment
import com.penseapp.acaocontabilidade.chat.contacts.view.ContactsView
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity.SELECTED_CHAT_KEY
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity.SELECTED_CHAT_NAME
import com.penseapp.acaocontabilidade.domain.FirebaseHelper
import com.penseapp.acaocontabilidade.domain.Preferences
import com.penseapp.acaocontabilidade.domain.Utilities
import com.penseapp.acaocontabilidade.login.view.activities.LoginActivity
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(),
        ContactsView,
        ChatsFragment.OnChatsFragmentInteractionListener,
        ContactsFragment.OnContactsFragmentInteractionListener {

    private var chatsWriterPresenter: ChatsWriterPresenterImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolBar()

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
//                    .replace(R.id.main_activity_fragment_holder, ChatsFragment.newInstance(this))
                    .replace(R.id.fragmentHolder, ContactsFragment.newInstance())
                    .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val fragmentHolder = supportFragmentManager.findFragmentById(R.id.fragmentHolder)
            when (fragmentHolder) {
                is ContactsFragment -> toolbar?.title = "Contatos"
                is ChatsFragment -> toolbar?.title = "Conversas"
                else -> toolbar?.title = "Ação"
            }
        }

        chatsWriterPresenter = ChatsWriterPresenterImpl(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_tabbed_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_acao_web) {
            Utilities.goToAcaoWebsite(this)
            return true
        }
        if (id == R.id.action_acao_facebook) {
            Utilities.goToAcaoFacebookWebsite(this)
            return true
        }
        if (id == R.id.action_questor_documents) {
            Utilities.goToDocumentsWebsite(this)
            return true
        }
        if (id == R.id.action_logout) {
            FirebaseHelper.getInstance().logout()
            navigateToLoginActivity()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onChatSelected(key: String?, name: String) {
        navigateToMessagesActivity(key, name)
    }

    override fun onShowContactsClicked() {
        showContactsFragment()
    }

    override fun onContactSelected(key: String, name: String, company: String) {
        createChatIfNeeded(key, name, company)
    }

    override fun onChatCreated(chatId: String, chatName: String) {
        navigateToMessagesActivity(chatId, chatName)
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
        toolbar.title = "Conversas"
    }

    private fun navigateToMessagesActivity(chatId: String?, chatName: String) {
        val intent = Intent(this, MessagesActivity::class.java)
        intent.putExtra(SELECTED_CHAT_KEY, chatId)
        intent.putExtra(SELECTED_CHAT_NAME, chatName)
        startActivity(intent)
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showContactsFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentHolder, ContactsFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }

    private fun createChatIfNeeded(contactId: String, contactName: String, company: String) {
        val user = Preferences.getUserFromPreferences(applicationContext)
        chatsWriterPresenter!!.createChatIfNeeded(user.key, user.name, user.company,
                contactId, contactName, company)
    }

}
