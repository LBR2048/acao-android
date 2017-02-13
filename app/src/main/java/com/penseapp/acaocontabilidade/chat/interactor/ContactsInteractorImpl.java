package com.penseapp.acaocontabilidade.chat.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
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
    private DatabaseReference usersReference = mFirebaseHelperInstance.getUsersReference();


    public ContactsInteractorImpl(ContactsPresenter contactsPresenter) {
        this.contactsPresenter = contactsPresenter;
    }

    @Override
    public void subscribeForContactsUpdates() {
        Log.i(LOG_TAG, "subscribeForContactsUpdates called, but listener already exists");

        if (contactsChildEventListener == null) {
            Log.i(LOG_TAG, "subscribeForContactsUpdates called");

            contactsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String contactKey = dataSnapshot.getKey();
                    try {
                        User contact = dataSnapshot.getValue(User.class);
                        contact.setKey(contactKey);
                        Log.i(LOG_TAG, dataSnapshot.getKey() + " added");
                        contactsPresenter.onContactAdded(contact);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error while reading chat " + contactKey);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    String contactKey = dataSnapshot.getKey();
                    try {
                        User contact = dataSnapshot.getValue(User.class);
                        contact.setKey(contactKey);
                        Log.i(LOG_TAG, dataSnapshot.getKey() + " changed");
                        contactsPresenter.onContactChanged(contact);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error while reading chat " + contactKey);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String contactKey = dataSnapshot.getKey();
                    try {
                        Log.i(LOG_TAG, dataSnapshot.getKey() + " removed");
                        contactsPresenter.onContactRemoved(contactKey);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error while reading chat " + contactKey);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            // TODO funciona, mas parece lento e a atividade tem que ser reiniciada para que a lista de contatos seja atualizada
            String currentUserId = mFirebaseHelperInstance.getAuthUserId();
            usersReference.child(currentUserId).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userType = dataSnapshot.getValue().toString();
                    if (userType.equals("customer")) {
                        // Show Ação list if you are a customer
                        usersReference.orderByChild("type").equalTo("acao").
                                addChildEventListener(contactsChildEventListener);
                    } else if (userType.equals("acao")) {
                        // Show customers list if you are an Ação employee
                        usersReference.orderByChild("type").equalTo("customer").
                                addChildEventListener(contactsChildEventListener);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void unsubscribeForContactsUpdates() {
        Log.i(LOG_TAG, "unsubscribeForContactsUpdates called, but listener already exists");

        if (contactsChildEventListener != null) {
            Log.i(LOG_TAG, "unsubscribeForContactsUpdates called");

            mFirebaseHelperInstance.getUsersReference().
                    removeEventListener(contactsChildEventListener);
        }
    }
}
