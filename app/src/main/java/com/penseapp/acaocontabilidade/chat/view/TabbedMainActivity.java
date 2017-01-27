package com.penseapp.acaocontabilidade.chat.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.presenter.ChatsPresenter;
import com.penseapp.acaocontabilidade.chat.presenter.ChatsPresenterImpl;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.login.view.activities.LoginActivity;
import com.penseapp.acaocontabilidade.news.view.NewsFragment;
import com.penseapp.acaocontabilidade.news.view.NewsItemActivity;

import static com.penseapp.acaocontabilidade.chat.view.ChatsActivity.SELECTED_CHAT_KEY;
import static com.penseapp.acaocontabilidade.chat.view.ChatsActivity.SELECTED_CHAT_NAME;

public class TabbedMainActivity extends AppCompatActivity implements
        ContactsView,
        ContactsFragment.OnContactsFragmentInteractionListener,
        NewsFragment.OnNewsFragmentInteractionListener {

    private static final String ACAO_FACEBOOK_URL = "https://www.facebook.com/acaocontabilidade/";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ChatsPresenter chatsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Set screen title
        String currentUserEmail = FirebaseHelper.getInstance().getAuthUserEmail();
        setTitle("Ação [" + currentUserEmail + " ]");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to Presenters
        chatsPresenter = new ChatsPresenterImpl(this);

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
        if (id == R.id.action_acao_facebook) {
            goToAcaoFacebookWebsite();
            return true;
        }
        if (id == R.id.action_logout) {
            logout();
            showLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            switch (position) {
                case 0:
                    return ContactsFragment.newInstance();
                case 1:
                    return NewsFragment.newInstance();
                default:
                    // Return a PlaceholderFragment (defined as a static inner class below).
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Chat";
                case 1:
                    return "Notícias";
                case 2:
                    return "Lembretes";
            }
            return null;
        }

    }
    public void goToAcaoFacebookWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(ACAO_FACEBOOK_URL));
        startActivity(intent);
    }

    @Override
    public void onContactSelected(String name, String key)  {
        chatsPresenter.createChatIfNeeded(name, key);
    }

    @Override
    public void onNewsSelected(String title, String key) {
        Intent intent = new Intent(this, NewsItemActivity.class);
        intent.putExtra(NewsItemActivity.SELECTED_NEWS_TITLE, title);
        intent.putExtra(NewsItemActivity.SELECTED_NEWS_KEY, key);
        startActivity(intent);
    }

    @Override
    public void onChatCreated(String chatId, String chatName) {
        Intent intent = new Intent(TabbedMainActivity.this, MessagesActivity.class);
        intent.putExtra(SELECTED_CHAT_KEY, chatId);
        intent.putExtra(SELECTED_CHAT_NAME, chatName);
        startActivity(intent);
    }

    private void showLoginActivity() {
        Intent intent = new Intent(TabbedMainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // TODO isso não deveria pertencer a essa atividade, pois não diz respeito ao View
    private void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
    }

}
