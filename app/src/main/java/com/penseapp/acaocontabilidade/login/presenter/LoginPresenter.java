package com.penseapp.acaocontabilidade.login.presenter;

/**
 * Created by Filip on 23/02/2016.
 */
public interface LoginPresenter {
    void signUp(String name, String email, String type, String password);
    void onSignUpFailure();
    void onSignUpSuccess(String email, String uid);

    void login(String email, String password);
    void onLoginFailure();
    void onLoginSuccess(String user, String uid);

    void resetPassword(String email);
    void onPasswordResetSuccess();
    void onPasswordResetFailure();

    void getCurrentUser();
    void onUserLoggedIn();
    void onUserLoggedOut();

    void addAuthStateListener();
    void removeAuthStateListener();
}
