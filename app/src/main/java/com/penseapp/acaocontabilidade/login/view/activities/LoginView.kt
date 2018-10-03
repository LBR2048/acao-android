package com.penseapp.acaocontabilidade.login.view.activities

/**
 * Created by Filip on 23/02/2016.
 */
interface LoginView {

    fun onSignUpFailure()

    fun onSignUpSuccess()


    fun onLoginSuccess()

    fun onLoginFailure()


    fun onPasswordResetSuccess()

    fun onPasswordResetFailure()


    fun onUserLoggedIn()

    fun onUserLoggedOut()
}
