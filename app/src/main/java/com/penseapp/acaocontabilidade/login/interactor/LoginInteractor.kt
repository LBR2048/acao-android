package com.penseapp.acaocontabilidade.login.interactor

/**
 * Created by unity on 24/02/2016.
 */
interface LoginInteractor {

    fun signUp(name: String, company: String, email: String, type: String, password: String)

    fun login(email: String, password: String)

    fun resetPassword(email: String)


    fun getCurrentUser()

    fun addAuthStateListener()

    fun removeAuthStateListener()
}
