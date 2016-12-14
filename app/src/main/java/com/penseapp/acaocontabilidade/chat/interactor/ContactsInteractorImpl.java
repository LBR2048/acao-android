package com.penseapp.acaocontabilidade.chat.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.penseapp.acaocontabilidade.chat.presenter.ContactsPresenter;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 21/11/16.
 */

public class ContactsInteractorImpl implements ContactsInteractor {

    private final static String LOG_TAG = ContactsInteractorImpl.class.getSimpleName();

    private final ContactsPresenter contactsPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private ChildEventListener contactsChildEventListener;

    public ContactsInteractorImpl(ContactsPresenter contactsPresenter) {
        this.contactsPresenter = contactsPresenter;
    }

    @Override
    public void subscribeForContactsUpdates() {
        Log.i(LOG_TAG, "Interactor " + LOG_TAG + " called");

        if (contactsChildEventListener == null) {
            contactsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i(LOG_TAG, dataSnapshot.toString() + " added");
                    User contact = dataSnapshot.getValue(User.class);
                    contactsPresenter.onContactAdded(contact);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.i(LOG_TAG, dataSnapshot.toString() + " changed");
                    User contact = dataSnapshot.getValue(User.class);
                    contactsPresenter.onContactChanged(contact);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.i(LOG_TAG, dataSnapshot.toString() + " removed");
                    String contactKey = dataSnapshot.getKey();
                    contactsPresenter.onContactRemoved(contactKey);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            // TODO
//            if (userType == "customer") {
//                // Show Ação sectors list if you are a customer
//                mFirebaseHelperInstance.getUsersReference().orderByChild("type").equalTo("acao").
//                        addChildEventListener(contactsChildEventListener);
//            } else if (userType == "acao") {
//                // Show customers list if you are an Ação employee
//                mFirebaseHelperInstance.getUsersReference().orderByChild("type").equalTo("customer").
//                        addChildEventListener(contactsChildEventListener);
//            }

//            mFirebaseHelperInstance.getUsersReference().orderByChild("type").equalTo("customer").
//                    addChildEventListener(contactsChildEventListener);
            mFirebaseHelperInstance.getUsersReference().orderByChild("type").equalTo("acao").
                    addChildEventListener(contactsChildEventListener);
//            mFirebaseHelperInstance.getUsersReference().
//                    addChildEventListener(contactsChildEventListener);
        }
    }

    @Override
    public void unsubscribeForContactsUpdates() {
        if (contactsChildEventListener != null) {
            mFirebaseHelperInstance.getUsersReference().
                    removeEventListener(contactsChildEventListener);
        }
    }
}
