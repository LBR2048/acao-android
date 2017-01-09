package com.penseapp.acaocontabilidade.chat.view;

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
import com.penseapp.acaocontabilidade.chat.adapters.ContactsAdapter;
import com.penseapp.acaocontabilidade.chat.presenter.ChatsPresenter;
import com.penseapp.acaocontabilidade.chat.presenter.ContactsPresenter;
import com.penseapp.acaocontabilidade.login.model.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContactsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {//implements ContactsView {

    private final static String LOG_TAG = ContactsActivity.class.getSimpleName();
    public static final int CONTACT_REQUEST = 0;
    public static final String SELECTED_CONTACT_NAME = "selected_contact";
    public static final String SELECTED_CONTACT_KEY = "selected_contact_key";
    private ContactsPresenter contactsPresenter;
    private ChatsPresenter chatsPresenter;

    public static ArrayList<User> mContacts = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private RecyclerView mContactsRecyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnContactsFragmentInteractionListener mListener;

    public ContactsFragment() {
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
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContactsRecyclerView = (RecyclerView) getActivity().findViewById(R.id.list_contacts);
        setupRecyclerView();
        setupRecyclerViewDecorator();

        // Connect to Presenters
//        contactsPresenter = new ContactsPresenterImpl(this);
//        chatsPresenter = new ChatsPresenterImpl(this);

        clearRecyclerView();
//        contactsPresenter.subscribeForContactsUpdates();

        // What happens when a contact from the list is clicked
        contactsAdapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                User selectedContact = mContacts.get(position);
                Log.i(LOG_TAG, selectedContact.getName() + " clicked");
                onContactClicked(selectedContact.getName(), selectedContact.getKey());

//                Intent data = new Intent();
//                // Pass relevant data back as a result
//                data.putExtra("name", etName.getText().toString());
//                data.putExtra("code", 200); // ints work too
//                // Activity finished ok, return the data
//                chatsPresenter.createChatIfNeeded(selectedContact.getName(), selectedContact.getKey());
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onContactClicked(String name, String key) {
        if (mListener != null) {
            mListener.onContactSelected(name, key) ;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactsFragmentInteractionListener) {
            mListener = (OnContactsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        contactsAdapter.unsubscribeForContactsUpdates();

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
    public interface OnContactsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onContactSelected(String name, String key) ;
    }

    private void setupRecyclerView() {
        contactsAdapter = new ContactsAdapter(mContacts);
        contactsAdapter.subscribeForContactsUpdates();
        mContactsRecyclerView.setAdapter(contactsAdapter);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupRecyclerViewDecorator() {
        // Display dividers between each item of the RecyclerView
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mContactsRecyclerView.addItemDecoration(itemDecoration);
    }

    private void clearRecyclerView() {
        mContacts.clear();
        contactsAdapter.notifyDataSetChanged(); // TODO not efficient
    }

//    @Override
//    public void onContactAdded(User contact) {
//        Log.i(LOG_TAG, "View onContactAdded called");
//        mContacts.add(contact);
//        contactsAdapter.notifyItemInserted(mContacts.size() - 1);
//    }
//
//    @Override
//    public void onContactChanged(User contact) {
//        Log.i(LOG_TAG, "View onContactChanged called");
//        int index = getIndexForKey(contact.getKey());
//        mContacts.set(index, contact);
//        contactsAdapter.notifyItemChanged(index);
//    }
//
//    @Override
//    public void onContactRemoved(String contactId) {
//        Log.i(LOG_TAG, "View onContactRemoved called");
//        try {
//            int index = getIndexForKey(contactId);
//            mChats.remove(index);
//            contactsAdapter.notifyItemRemoved(index);
//        } catch(IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onChatCreated(String chatId, String chatName) {
//        Intent intent = new Intent(getActivity(), MessagesActivity.class);
//        intent.putExtra(SELECTED_CHAT_KEY, chatId);
//        intent.putExtra(SELECTED_CHAT_NAME, chatName);
//        startActivity(intent);
//    }
//
//    // TODO this method should go somewhere else? Does it belong in the View?
//    // TODO duplicado em ExerciseChooserActivity
//    private int getIndexForKey(String key) {
//        int index = 0;
//        for (User contact : mContacts) {
//            if (contact.getKey().equals(key)) {
//                return index;
//            } else {
//                index++;
//            }
//        }
//        throw new IllegalArgumentException("Key not found");
//    }
}
