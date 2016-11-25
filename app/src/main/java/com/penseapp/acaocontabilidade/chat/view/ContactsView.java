package com.penseapp.acaocontabilidade.chat.view;

import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 21/11/16.
 */

public interface ContactsView {
    void onContactAdded(User contact);
    void onContactChanged(User contact);
    void onContactRemoved(String contactId);
}
