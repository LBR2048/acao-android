package com.penseapp.acaocontabilidade.chat.contacts.interactor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.penseapp.acaocontabilidade.chat.contacts.presenter.ContactsPresenter;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 21/11/16.
 */

public class ContactsInteractorImpl implements ContactsInteractor {

    private final static String LOG_TAG = ContactsInteractorImpl.class.getSimpleName();

    private final ContactsPresenter contactsPresenter;

    // Firebase
    private final FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private ChildEventListener contactsChildEventListener;
    private final DatabaseReference usersReference = mFirebaseHelperInstance.getUsersReference();


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
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    final User contact = dataSnapshot.getValue(User.class);
                    if (contact != null) {
                        contact.setKey(dataSnapshot.getKey());
                        contactsPresenter.onContactAdded(contact);
                        Log.i(LOG_TAG, "Chat added " + dataSnapshot.getKey());
                    } else {
                        Log.e(LOG_TAG, "Error reading chat " + dataSnapshot.getKey());
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                    final User contact = dataSnapshot.getValue(User.class);
                    if (contact != null){
                        contact.setKey(dataSnapshot.getKey());
                        contactsPresenter.onContactChanged(contact);
                        Log.i(LOG_TAG, "Chat changed " + dataSnapshot.getKey());
                    } else {
                        Log.e(LOG_TAG, "Error reading chat " + dataSnapshot.getKey());
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    final String contactKey = dataSnapshot.getKey();
                    if (contactKey != null) {
                        contactsPresenter.onContactRemoved(contactKey);
                        Log.i(LOG_TAG, "Chat removed " + dataSnapshot.getKey());
                    } else {
                        Log.e(LOG_TAG, "Error reading chat");
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            // TODO funciona, mas parece lento e a atividade tem que ser reiniciada para que a lista de contatos seja atualizada
            String currentUserId = mFirebaseHelperInstance.getAuthUserId();
            usersReference.child(currentUserId).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
