package com.penseapp.acaocontabilidade.login.presenter;


import android.util.Log;

import com.penseapp.acaocontabilidade.login.interactor.LoginInteractorImpl;
import com.penseapp.acaocontabilidade.login.view.activities.LoginView;

/**
 * Created by unity on 21/11/2016.
 */
public class LoginPresenterImpl implements LoginPresenter {

    private static final String LOG_TAG = LoginPresenterImpl.class.getSimpleName();
    private final LoginView loginView;
    private final LoginInteractorImpl interactor;

    public LoginPresenterImpl(LoginView view) {
        this.loginView = view;
        interactor = new LoginInteractorImpl(this);
    }


    // Sign Up

    @Override
    public void signUp(String name, String email, String type, String password) {
        interactor.signUp(name, email, type, password);
    }

    @Override
    public void onSignUpSuccess(String email, String uid) {
        Log.i(LOG_TAG, "Sign Up Success");
        loginView.onSignUpSuccess();
    }

    @Override
    public void onSignUpFailure() {
        Log.i(LOG_TAG, "Sign Up Failure");
        loginView.onSignUpFailure();
    }


    // Login

    @Override
    public void login(String email, String password) {
        interactor.login(email, password);
    }

    @Override
    public void onLoginFailure() {
        loginView.onLoginFailure();
    }

    @Override
    public void onLoginSuccess(String user, String uid) {
        Log.i(LOG_TAG, "Login Success");
        loginView.onLoginSuccess();
    }

    // Reset password

    @Override
    public void resetPassword(String email) {
        interactor.resetPassword(email);
    }

    @Override
    public void onPasswordResetSuccess() {
        loginView.onPasswordResetSuccess();
    }

    @Override
    public void onPasswordResetFailure() {
        loginView.onPasswordResetFailure();
    }


    // Authorization state

    @Override
    public void getCurrentUser() {

        interactor.getCurrentUser();
    }

    @Override
    public void addAuthStateListener() {
        interactor.addAuthStateListener();
    }

    @Override
    public void removeAuthStateListener() {
        interactor.removeAuthStateListener();
    }

    @Override
    public void onUserLoggedIn() {
        loginView.onUserLoggedIn();
    }

    @Override
    public void onUserLoggedOut() {
        loginView.onUserLoggedOut();
    }

}