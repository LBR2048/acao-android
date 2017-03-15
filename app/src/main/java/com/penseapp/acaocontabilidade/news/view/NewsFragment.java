package com.penseapp.acaocontabilidade.news.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.view.ContactsActivity;
import com.penseapp.acaocontabilidade.news.adapter.NewsAdapter;
import com.penseapp.acaocontabilidade.news.model.News;
import com.penseapp.acaocontabilidade.news.presenter.NewsNotificationsPresenter;
import com.penseapp.acaocontabilidade.news.presenter.NewsNotificationsPresenterImpl;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNewsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements NewsView {

    private final static String LOG_TAG = ContactsActivity.class.getSimpleName();

    private NewsNotificationsPresenter newsNotificationsPresenter;

    public static ArrayList<News> mNews = new ArrayList<>();
    private NewsAdapter newsAdapter;
    private RecyclerView mContactsRecyclerView;
        private OnNewsFragmentInteractionListener mListener;
    private MenuItem receiveNotificationsItem;

    public NewsFragment() {
        // Required empty public constructor
        newsNotificationsPresenter = new NewsNotificationsPresenterImpl(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_news, menu);
        receiveNotificationsItem = menu.findItem(R.id.action_subscribe_news);
//        isSubscribedToNewsNotifications();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_subscribe_news) {
            if (item.isChecked()) {
                // If item already checked then unchecked it
                item.setChecked(false);
                unsubscribeFromNewsNotifications();
            } else {
                // If item is unchecked then check it
                item.setChecked(true);
                subscribeToNewsNotifications();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContactsRecyclerView = (RecyclerView) getActivity().findViewById(R.id.list_news);
        setupRecyclerView();
        setupRecyclerViewDecorator();
        clearRecyclerView();

        // What happens when a news item from the list is clicked
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                News selectedNews = mNews.get(position);
                Log.i(LOG_TAG, selectedNews.getTitle() + " clicked");
                onNewsClicked(selectedNews.getTitle(), selectedNews.getKey());
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onNewsClicked(String name, String key) {
        if (mListener != null) {
            mListener.onNewsSelected(key, name) ;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewsFragmentInteractionListener) {
            mListener = (OnNewsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        newsAdapter.unsubscribeForNewsUpdates();

        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNewsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNewsSelected(String key, String title) ;
    }

    private void setupRecyclerView() {
        newsAdapter = new NewsAdapter(mNews);
        newsAdapter.subscribeForNewsUpdates();
        mContactsRecyclerView.setAdapter(newsAdapter);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupRecyclerViewDecorator() {
        // Display dividers between each item of the RecyclerView
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mContactsRecyclerView.addItemDecoration(itemDecoration);
    }

    private void clearRecyclerView() {
        mNews.clear();
        newsAdapter.notifyDataSetChanged(); // TODO not efficient
    }


    @Override
    public void subscribeToNewsNotifications() {
        newsNotificationsPresenter.subscribeToNewsNotifications();
    }

    @Override
    public void unsubscribeFromNewsNotifications() {
        newsNotificationsPresenter.unsubscribeFromNewsNotifications();
    }

    @Override
    public void isSubscribedToNewsNotifications() {
        newsNotificationsPresenter.isSubscribedToNewsNotifications();
    }

    @Override
    public void onReceiveNewsNotificationsSubscriptionStatus(boolean isSubscribed) {
        if (isSubscribed) {
            receiveNotificationsItem.setChecked(true);
//            subscribeToNewsNotifications();
        } else { // If false or null
            receiveNotificationsItem.setChecked(false);
//            unsubscribeFromNewsNotifications();
        }
    }
}
