package com.penseapp.acaocontabilidade.chat.contacts.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private final static String LOG_TAG = ContactsFragment.class.getSimpleName();

    private static final ArrayList<User> mContacts = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private RecyclerView mContactsRecyclerView;
    private OnContactsFragmentInteractionListener mListener;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContactsRecyclerView = view.findViewById(R.id.list_contacts);
        setupRecyclerView();
        setupRecyclerViewDecorator();
        clearRecyclerView();

        // What happens when a contact from the list is clicked
        contactsAdapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                User selectedContact = mContacts.get(position);
                Log.i(LOG_TAG, selectedContact.getName() + " clicked");
                onContactClicked(selectedContact.getKey(), selectedContact.getName(), selectedContact.getCompany());
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void onContactClicked(String key, String name, String company) {
        if (mListener != null) {
            mListener.onContactSelected(key, name, company) ;
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
        // TODO Attempt to invoke virtual method 'void com.penseapp.acaocontabilidade.chat.contacts.view.ContactsAdapter.unsubscribeForContactsUpdates()' on a null object reference
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
        void onContactSelected(String key, String name, String company) ;
    }

    private void setupRecyclerView() {
        contactsAdapter = new ContactsAdapter(mContacts);
        contactsAdapter.subscribeForContactsUpdates();
        mContactsRecyclerView.setAdapter(contactsAdapter);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupRecyclerViewDecorator() {
        // Display dividers between each item of the RecyclerView
        if (getContext() != null){
            RecyclerView.ItemDecoration itemDecoration = new
                    DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            mContactsRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    private void clearRecyclerView() {
        mContacts.clear();
        contactsAdapter.notifyDataSetChanged(); // TODO not efficient
    }
}
