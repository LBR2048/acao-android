package com.penseapp.acaocontabilidade

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsWriterPresenter
import com.penseapp.acaocontabilidade.chat.chats.presenter.ChatsWriterPresenterImpl
import com.penseapp.acaocontabilidade.chat.chats.view.ChatsFragment
import com.penseapp.acaocontabilidade.chat.contacts.view.ContactsFragment
import com.penseapp.acaocontabilidade.chat.contacts.view.ContactsView
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity.SELECTED_CHAT_KEY
import com.penseapp.acaocontabilidade.chat.messages.view.MessagesActivity.SELECTED_CHAT_NAME
import com.penseapp.acaocontabilidade.chat.users.presenter.UsersPresenter
import com.penseapp.acaocontabilidade.chat.users.presenter.UsersPresenterImpl
import com.penseapp.acaocontabilidade.chat.users.view.UsersView
import com.penseapp.acaocontabilidade.domain.FirebaseHelper
import com.penseapp.acaocontabilidade.domain.Preferences
import com.penseapp.acaocontabilidade.domain.Utilities
import com.penseapp.acaocontabilidade.login.model.User
import com.penseapp.acaocontabilidade.login.view.activities.LoginActivity
import com.penseapp.acaocontabilidade.news.view.NewsFragment
import com.penseapp.acaocontabilidade.news.view.NewsItemActivity
import com.penseapp.acaocontabilidade.web.WebFragment
import kotlinx.android.synthetic.main.activity_tabbed_main.*
import kotlinx.android.synthetic.main.fragment_tabbed_main.*

class TabbedMainActivity : AppCompatActivity(),
        ContactsView,
        UsersView,
        ContactsFragment.OnContactsFragmentInteractionListener,
        ChatsFragment.OnChatsFragmentInteractionListener,
        NewsFragment.OnNewsFragmentInteractionListener {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var chatsWriterPresenter: ChatsWriterPresenter? = null
    private var usersPresenter: UsersPresenter? = null

    //region Lifecycle and menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed_main)

        setSupportActionBar(toolbar)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        viewPager.adapter = mSectionsPagerAdapter

        tabLayout.setupWithViewPager(viewPager)

        // Set screen title
        val currentUserEmail = FirebaseHelper.getInstance().authUserEmail
        //        setTitle("Ação [" + currentUserEmail + " ]");
    }

    override fun onStart() {
        super.onStart()

        // Connect to Presenters
        chatsWriterPresenter = ChatsWriterPresenterImpl(this)
        usersPresenter = UsersPresenterImpl(this)

        // Get current user details
        usersPresenter!!.getCurrentUserDetails()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.action_logout) {
            FirebaseHelper.getInstance().logout()
            navigateToLoginActivity()
        }

        return super.onOptionsItemSelected(item)
    }
    //endregion

    //region Tabs
    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_tabbed_main, container, false)
            sectionLabel.text = getString(R.string.section_format, arguments!!.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private const val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            internal fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    internal inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.

            return when (position) {
                0 -> ContactsFragment.newInstance()
                1 -> NewsFragment.newInstance()
                2 -> WebFragment.newInstance("http://acaocont.app.questorpublico.com.br/entrar")
                else ->
                    // Return a PlaceholderFragment (defined as a static inner class below).
                    PlaceholderFragment.newInstance(position + 1)
            }
        }

        override fun getCount(): Int {
            return NUMBER_TABS
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Conversas"
                1 -> return "Notícias"
                2 -> return "Documentos"
            }
            return null
        }

    }
    //endregion

    //region UserView callbacks
    override fun onReceiveCurrentUserDetails(user: User) {
        Preferences.saveUserPreferences(applicationContext, user)
    }
    //endregion

    //region ContactsView callbacks
    override fun onChatCreated(chatId: String, chatName: String) {
        navigateToMessagesActivity(chatId, chatName)
    }
    //endregion

    //region Fragments callbacks
    override fun onContactSelected(contactId: String, contactName: String, company: String) {
        createChatIfNeeded(contactId, contactName, company)
    }

    override fun onChatSelected(chatId: String?, chatName: String) {
        navigateToMessagesActivity(chatId, chatName)
    }

    override fun onShowContactsClicked() {

    }

    override fun onNewsSelected(newsId: String, newsTitle: String) {
        navigateToNewsActivity(newsId, newsTitle)
    }
    //endregion

    //region Helper methods
    private fun navigateToMessagesActivity(chatId: String?, chatName: String) {
        val intent = Intent(this@TabbedMainActivity, MessagesActivity::class.java)
        intent.putExtra(SELECTED_CHAT_KEY, chatId)
        intent.putExtra(SELECTED_CHAT_NAME, chatName)
        startActivity(intent)
    }

    private fun navigateToNewsActivity(newsId: String, newsTitle: String) {
        val intent = Intent(this, NewsItemActivity::class.java)
        intent.putExtra(NewsItemActivity.SELECTED_NEWS_TITLE, newsTitle)
        intent.putExtra(NewsItemActivity.SELECTED_NEWS_KEY, newsId)
        startActivity(intent)
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun createChatIfNeeded(contactId: String, contactName: String, company: String) {
        val user = Preferences.getUserFromPreferences(applicationContext)
        chatsWriterPresenter!!.createChatIfNeeded(user.key, user.name, user.company,
                contactId, contactName, company)
    }
    //endregion

    companion object {

        private const val NUMBER_TABS = 3
    }

}
