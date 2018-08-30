package com.penseapp.acaocontabilidade.login.view.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.penseapp.acaocontabilidade.MainActivity
import com.penseapp.acaocontabilidade.R
import com.penseapp.acaocontabilidade.domain.FirebaseHelper
import com.penseapp.acaocontabilidade.domain.Utilities
import com.penseapp.acaocontabilidade.login.presenter.LoginPresenterImpl
import com.penseapp.acaocontabilidade.login.view.fragments.LoginFragment
import com.penseapp.acaocontabilidade.login.view.fragments.ResetPasswordFragment
import com.penseapp.acaocontabilidade.login.view.fragments.SignUpFragment

class LoginActivity : AppCompatActivity(),
        LoginView,
        LoginFragment.OnFragmentInteractionListener,
        ResetPasswordFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener {

    private lateinit var presenter: LoginPresenterImpl

    //region Activity lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = LoginPresenterImpl(this)
        presenter.getCurrentUser()
    }

    public override fun onStart() {
        super.onStart()
        presenter.addAuthStateListener()
    }

    public override fun onStop() {
        super.onStop()
        presenter.removeAuthStateListener()
    }
    //endregion

    //region GUI interaction
    override fun onLoginFragmentLoginClicked(email: String, password: String) {
        Utilities.hideKeyboard(this)
        presenter.login(email, password)
    }

    override fun onLoginFragmentForgotPasswordClicked() {
        showResetPasswordFragment()
    }

    override fun onLoginFragmentSignUpClicked() {
        showSignUpFragment()
    }

    override fun onResetPasswordFragmentResetClicked(email: String) {
        presenter.resetPassword(email)
    }

    override fun onSignUpFragmentSignUpClicked(name: String, company: String, email: String, type: String, password: String) {
        presenter.signUp(name, company, email, type, password)
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        }
    }
    //endregion

    //region Navigation
    private fun showMainActivity() {
        //        Intent intent = new Intent(LoginActivity.this, TabbedMainActivity.class);
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        // Ao pressionar Logout, a WorkoutsActivity não pode mais ser acessada através do back button
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoginFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.replace_frame, LoginFragment.newInstance(), LOGIN_FRAGMENT_TAG)
                .commit()
    }

    private fun showResetPasswordFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.replace_frame, ResetPasswordFragment.newInstance(), RESET_PASSWORD_FRAGMENT_TAG)
                .addToBackStack(RESET_PASSWORD_FRAGMENT_TAG)
                .commit()
    }

    private fun showSignUpFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.replace_frame, SignUpFragment.newInstance(), SIGN_UP_FRAGMENT_TAG)
                .addToBackStack(SIGN_UP_FRAGMENT_TAG)
                .commit()
    }
    //endregion

    //region Sign up callbacks
    override fun onSignUpSuccess() {
        Toast.makeText(applicationContext, "${FirebaseHelper.getInstance().authUserEmail!!} registrado com sucesso", Toast.LENGTH_SHORT).show()
    }

    override fun onSignUpFailure() {
        Toast.makeText(applicationContext, R.string.login_register_error, Toast.LENGTH_SHORT).show()
    }
    //endregion

    //region Login callbacks
    override fun onLoginSuccess() {
        Toast.makeText(applicationContext, "Logado como ${FirebaseHelper.getInstance().authUserEmail!!}", Toast.LENGTH_SHORT).show()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        FirebaseHelper.getInstance().sendFcmTokenToServer(refreshedToken)
        Log.d(LOG_TAG, "Token: " + refreshedToken!!)
        showMainActivity()
    }

    override fun onLoginFailure() {
        Toast.makeText(applicationContext, R.string.login_error, Toast.LENGTH_SHORT).show()
    }
    //endregion

    //region Password reset callbacks
    override fun onPasswordResetSuccess() {
        Toast.makeText(applicationContext, R.string.login_reset_password_success, Toast.LENGTH_SHORT).show()
    }

    override fun onPasswordResetFailure() {
        Toast.makeText(applicationContext, R.string.login_reset_password_failure, Toast.LENGTH_SHORT).show()
    }
    //endregion

    //region Authorization state callbacks
    override fun onUserLoggedIn() {
        showMainActivity()
        Log.d(LOG_TAG, "onAuthStateChanged:signed_in")
    }

    override fun onUserLoggedOut() {
        setContentView(R.layout.activity_login)
        showLoginFragment()
        Log.d(LOG_TAG, "onAuthStateChanged:signed_out")
    }

    companion object {

        //region Constants
        private val LOG_TAG = LoginActivity::class.java.simpleName
        private const val LOGIN_FRAGMENT_TAG = "login"
        private const val RESET_PASSWORD_FRAGMENT_TAG = "reset"
        private const val SIGN_UP_FRAGMENT_TAG = "signup"
    }
    //endregion

}