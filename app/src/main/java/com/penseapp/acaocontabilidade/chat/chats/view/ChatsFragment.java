package com.penseapp.acaocontabilidade.chat.chats.view;

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
import com.penseapp.acaocontabilidade.chat.chats.model.Chat;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnChatsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment {

    private final static String LOG_TAG = ChatsFragment.class.getSimpleName();

    private static final ArrayList<Chat> mChats = new ArrayList<>();
    private OnChatsFragmentInteractionListener mListener;
    private RecyclerView mChatsRecyclerView;
    private ChatsAdapter chatsAdapter;

    public ChatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatsFragment.
     * @param context
     */
    public static ChatsFragment newInstance(Context context) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChatsRecyclerView = view.findViewById(R.id.list_chats);
        setupRecyclerView();
        setupRecyclerViewDecorator();
        clearRecyclerView();

        // What happens when a chat from the list is clicked
        chatsAdapter.setOnItemClickListener(new ChatsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Chat selectedChat = mChats.get(position);
                Log.i(LOG_TAG, selectedChat.getName() + " clicked");
                onChatClicked(selectedChat.getName(), selectedChat.getKey());
            }
        });
    }

    private void onChatClicked(String name, String key) {
        if (mListener != null) {
            mListener.onChatSelected(key, name);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatsFragmentInteractionListener) {
            mListener = (OnChatsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        chatsAdapter.unsubscribeForChatListUpdates();

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
    public interface OnChatsFragmentInteractionListener {
        void onChatSelected(String key, String name);
    }

    private void setupRecyclerView() {
        chatsAdapter = new ChatsAdapter(mChats, getContext());
        chatsAdapter.subscribeForChatsUpdates();
        mChatsRecyclerView.setAdapter(chatsAdapter);
        mChatsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupRecyclerViewDecorator() {
        // Display dividers between each item of the RecyclerView
        if (getContext() != null) {
            RecyclerView.ItemDecoration itemDecoration = new
                    DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            mChatsRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    private void clearRecyclerView() {
        mChats.clear();
        chatsAdapter.notifyDataSetChanged();
    }
}
