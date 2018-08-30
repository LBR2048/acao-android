package com.penseapp.acaocontabilidade.login.presenter

/**
 * Created by Filip on 23/02/2016.
 */
interface LoginPresenter {

    fun signUp(name: String, company: String, email: String, type: String, password: String)

    fun onSignUpFailure()

    fun onSignUpSuccess(email: String, uid: String)


    fun login(email: String, password: String)

    fun onLoginFailure()

    fun onLoginSuccess(user: String, uid: String)


    fun resetPassword(email: String)

    fun onPasswordResetSuccess()

    fun onPasswordResetFailure()


    fun getCurrentUser()

    fun onUserLoggedIn()

    fun onUserLoggedOut()


    fun addAuthStateListener()

    fun removeAuthStateListener()
}
