package com.penseapp.acaocontabilidade.chat.adapters;

import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 14/12/16.
 */

public interface ContactsAdapterView {
    void subscribeForContactsUpdates();
    void unsubscribeForContactsUpdates();
    void onContactAdded(User contact);
    void onContactChanged(User contact);
    void onContactRemoved(String contactId);
}
