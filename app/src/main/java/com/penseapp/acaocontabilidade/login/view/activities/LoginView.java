package com.penseapp.acaocontabilidade.login.view.activities;

/**
 * Created by Filip on 23/02/2016.
 */
public interface LoginView {
    void onSignUpFailure();
    void onSignUpSuccess();

    void onLoginSuccess();
    void onLoginFailure();

    void onUserLoggedIn();
    void onUserLoggedOut();
}
