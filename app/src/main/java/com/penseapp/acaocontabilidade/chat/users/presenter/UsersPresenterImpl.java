package com.penseapp.acaocontabilidade.chat.users.presenter;

import com.penseapp.acaocontabilidade.chat.users.interactor.UsersInteractor;
import com.penseapp.acaocontabilidade.chat.users.interactor.UsersInteractorImpl;
import com.penseapp.acaocontabilidade.chat.users.view.UsersView;
import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 08/02/17.
 */

public class UsersPresenterImpl implements UsersPresenter {

    private UsersView usersView;
    private UsersInteractor usersInteractor;

    public UsersPresenterImpl(UsersView usersView) {
        this.usersView = usersView;
        this.usersInteractor = new UsersInteractorImpl(this);
    }

    @Override
    public void getCurrentUserDetails() {
        usersInteractor.getCurrentUserDetails();
    }

    @Override
    public void onReceiveCurrentUserDetails(User user) {
        usersView.onReceiveCurrentUserDetails(user);
    }
}
