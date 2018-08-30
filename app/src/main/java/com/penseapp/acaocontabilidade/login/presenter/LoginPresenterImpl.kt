package com.penseapp.acaocontabilidade.login.presenter

import android.util.Log

import com.penseapp.acaocontabilidade.login.interactor.LoginInteractorImpl
import com.penseapp.acaocontabilidade.login.view.activities.LoginView

class LoginPresenterImpl(private val loginView: LoginView) : LoginPresenter {

    private val interactor: LoginInteractorImpl = LoginInteractorImpl(this)

    //region Sign Up
    override fun signUp(name: String, company: String, email: String, type: String, password: String) {
        interactor.signUp(name, company, email, type, password)
    }

    override fun onSignUpSuccess(email: String, uid: String) {
        Log.i(LOG_TAG, "Sign Up Success")
        loginView.onSignUpSuccess()
    }

    override fun onSignUpFailure() {
        Log.i(LOG_TAG, "Sign Up Failure")
        loginView.onSignUpFailure()
    }
    //endregion

    //region Login
    override fun login(email: String, password: String) {
        interactor.login(email, password)
    }

    override fun onLoginFailure() {
        loginView.onLoginFailure()
    }

    override fun onLoginSuccess(user: String, uid: String) {
        Log.i(LOG_TAG, "Login Success")
        loginView.onLoginSuccess()
    }
    //endregion

    //region Reset password
    override fun resetPassword(email: String) {
        interactor.resetPassword(email)
    }

    override fun onPasswordResetSuccess() {
        loginView.onPasswordResetSuccess()
    }

    override fun onPasswordResetFailure() {
        loginView.onPasswordResetFailure()
    }
    //endregion

    //region Authorization state
    override fun getCurrentUser() {
        interactor.getCurrentUser()
    }

    override fun addAuthStateListener() {
        interactor.addAuthStateListener()
    }

    override fun removeAuthStateListener() {
        interactor.removeAuthStateListener()
    }

    override fun onUserLoggedIn() {
        loginView.onUserLoggedIn()
    }

    override fun onUserLoggedOut() {
        loginView.onUserLoggedOut()
    }
    //endregion

    companion object {

        private val LOG_TAG = LoginPresenterImpl::class.java.simpleName
    }

}