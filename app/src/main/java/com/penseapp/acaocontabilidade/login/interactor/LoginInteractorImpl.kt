package com.penseapp.acaocontabilidade.login.interactor

import com.google.firebase.auth.FirebaseAuth
import com.penseapp.acaocontabilidade.domain.FirebaseHelper
import com.penseapp.acaocontabilidade.login.presenter.LoginPresenter

class LoginInteractorImpl(private val presenter: LoginPresenter) : LoginInteractor {

    // Firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun signUp(name: String, company: String, email: String, type: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val userEmail = authResult.user.email
                    val userId = authResult.user.uid
                    // TODO create constants for Strings or use object to write to Firebase
                    val firebaseHelper = FirebaseHelper.getInstance()
                    with (firebaseHelper.usersReference.child(userId)) {
                        child("name").setValue(name)
                        child("company").setValue(company)
                        child("email").setValue(email)
                        child("type").setValue(type)
                    }

                    presenter.onSignUpSuccess(userEmail, userId)
                }
                .addOnFailureListener { presenter.onSignUpFailure() }
    }

    override fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val userEmail = authResult.user.email
                    val userId = authResult.user.uid
                    presenter.onLoginSuccess(userEmail, userId)
                }
                .addOnFailureListener { presenter.onLoginFailure() }
    }

    override fun resetPassword(email: String) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        presenter.onPasswordResetSuccess()
                    } else {
                        presenter.onPasswordResetFailure()
                    }
                }
    }

    override fun getCurrentUser() {
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                presenter.onUserLoggedIn()
            } else {
                // User is signed out
                presenter.onUserLoggedOut()
            }
        }
    }

    override fun addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthListener!!)
    }

    override fun removeAuthStateListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener!!)
        }
    }
}
