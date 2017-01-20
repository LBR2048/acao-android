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
import android.view.View;
import android.view.ViewGroup;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.view.ContactsActivity;
import com.penseapp.acaocontabilidade.news.adapter.NewsAdapter;
import com.penseapp.acaocontabilidade.news.model.News;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNewsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {//implements ContactsView {

    private final static String LOG_TAG = ContactsActivity.class.getSimpleName();

    public static ArrayList<News> mNews = new ArrayList<>();
    // TODO retirar getkey
    private List<String> mNewsKeys;
    private NewsAdapter newsAdapter;
    private RecyclerView mContactsRecyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnNewsFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                // TODO
//                String newsKey = mNewsKeys.get(position);
                Log.i(LOG_TAG, selectedNews.getTitle() + " clicked");
                onNewsClicked(selectedNews.getTitle(), selectedNews.getKey());
                // TODO
//                onNewsClicked(selectedNews.getTitle(), newsKey);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onNewsClicked(String name, String key) {
        if (mListener != null) {
            mListener.onNewsSelected(name, key) ;
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
        void onNewsSelected(String title, String key) ;
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
        // TODO
//        mNewsKeys.clear();
        newsAdapter.notifyDataSetChanged(); // TODO not efficient
    }
}
