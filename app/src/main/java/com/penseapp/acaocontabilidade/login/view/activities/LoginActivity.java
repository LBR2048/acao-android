package com.penseapp.acaocontabilidade.login.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.view.TabbedMainActivity;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.login.presenter.LoginPresenterImpl;
import com.penseapp.acaocontabilidade.login.view.fragments.LoginFragment;
import com.penseapp.acaocontabilidade.login.view.fragments.SignUpFragment;


/**
 * Created by Filip on 23/02/2016.
 */
public class LoginActivity extends AppCompatActivity implements
        LoginView,
        LoginFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener {

    private static String LOG_TAG = LoginActivity.class.getSimpleName();

    private LoginPresenterImpl presenter;

    public static final String LOGIN_FRAGMENT_TAG = "login";
    public static final String SIGNUP_FRAGMENT_TAG = "signup";


    // Activity lifecycle

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenterImpl(this);
        presenter.getCurrentUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.addAuthStateListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.removeAuthStateListener();
    }


    // GUI interaction

    @Override
    public void onLoginFragmentLoginClicked(String email, String password) {
//        if (!validateEmail(email))
//            mUsernameWrapper.setError("Not a valid email address!");
//        else if (!validatePassword(password))
//            mPasswordWrapper.setError("Not a valid password!");
//        else {
            hideKeyboard();
            presenter.login(email, password);
//        }
    }

    @Override
    public void onLoginFragmentSignUpClicked() {
        Toast.makeText(getApplicationContext(), "Sign Up", Toast.LENGTH_SHORT).show();
        showSignUpFragment();
    }

    @Override
    public void onSignUpFragmentSignUpClicked(String name, String email, String type, String password) {
        Toast.makeText(getApplicationContext(), "SignUp clicked", Toast.LENGTH_SHORT).show();
        presenter.signUp(name, email, type, password);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }


    // Navigation

    private void showMainActivity() {
        Intent intent = new Intent(LoginActivity.this, TabbedMainActivity.class);
        // TODO Ao pressionar Logout, a WorkoutsActivity não pode mais ser acessada através do back button
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showLoginFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.replace_frame, LoginFragment.newInstance(), LOGIN_FRAGMENT_TAG)
                .commit();
    }

    private void showSignUpFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.replace_frame, SignUpFragment.newInstance(), SIGNUP_FRAGMENT_TAG)
                .addToBackStack(SIGNUP_FRAGMENT_TAG)
                .commit();
    }


    // Sign up callbacks

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(getApplicationContext(), "Sign Up successful for user " + FirebaseHelper.getInstance().getAuthUserEmail(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpFailure() {
        Toast.makeText(LoginActivity.this, "Sign up failure", Toast.LENGTH_SHORT).show();
    }


    // Login callbacks

    @Override
    public void onLoginSuccess() {
        Toast.makeText(getApplicationContext(), "Logged in as " + FirebaseHelper.getInstance().getAuthUserEmail(), Toast.LENGTH_SHORT).show();
        showMainActivity();
    }

    @Override
    public void onLoginFailure() {
        Toast.makeText(LoginActivity.this, "Login failure", Toast.LENGTH_SHORT).show();
    }


    // Authorization state callbacks

    @Override
    public void onUserLoggedIn() {
        // User is signed in
        showMainActivity();
        Log.d(LOG_TAG, "onAuthStateChanged:signed_in");
    }

    @Override
    public void onUserLoggedOut() {
        // User is signed out
        setContentView(R.layout.activity_login);
        showLoginFragment();
        Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
    }


    // TODO mover para utilities ou para uma super classe Activity
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}