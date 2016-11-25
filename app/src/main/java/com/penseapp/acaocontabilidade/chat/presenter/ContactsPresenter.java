package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 21/11/16.
 */

public interface ContactsPresenter {
    void subscribeForContactsUpdates();
    void unsubscribeForContactsUpdates();
    void onContactAdded(User contact);
    void onContactChanged(User contact);
    void onContactRemoved(String contactId);
}
