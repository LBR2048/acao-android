package com.penseapp.acaocontabilidade.chat.presenter;

import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 08/02/17.
 */

public interface UsersPresenter {
    void getCurrentUserDetails();
    void onReceiveCurrentUserDetails(User user);
}
