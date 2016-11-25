package com.penseapp.acaocontabilidade.login.interactor;

/**
 * Created by unity on 24/02/2016.
 */
public interface LoginInteractor {
    void signUp(String name, String user, String type, String password);
    void login(String email, String password);

    void getCurrentUser();
    void addAuthStateListener();
    void removeAuthStateListener();
}
