package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.chat.interactor.ContactsInteractor;
import com.penseapp.acaocontabilidade.chat.interactor.ContactsInteractorImpl;
import com.penseapp.acaocontabilidade.chat.view.ContactsView;
import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 21/11/16.
 */

public class ContactsPresenterImpl implements ContactsPresenter {

    private final ContactsView contactsView;
    private final ContactsInteractor contactsInteractor;

    public ContactsPresenterImpl(ContactsView contactsView) {
        this.contactsView = contactsView;
        this.contactsInteractor = new ContactsInteractorImpl(this);
    }

    @Override
    public void subscribeForContactsUpdates() {
        contactsInteractor.subscribeForContactsUpdates();
    }

    @Override
    public void unsubscribeForContactsUpdates() {
        contactsInteractor.unsubscribeForContactsUpdates();
    }

    @Override
    public void onContactAdded(User contact) {
        contactsView.onContactAdded(contact);
    }

    @Override
    public void onContactChanged(User contact) {
        contactsView.onContactChanged(contact);
    }

    @Override
    public void onContactRemoved(String contactId) {
        contactsView.onContactRemoved(contactId);
    }
}
