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
import com.penseapp.acaocontabilidade.login.view.fragments.ResetPasswordFragment;
import com.penseapp.acaocontabilidade.login.view.fragments.SignUpFragment;


/**
 * Created by Filip on 23/02/2016.
 */
public class LoginActivity extends AppCompatActivity implements
        LoginView,
        LoginFragment.OnFragmentInteractionListener,
        ResetPasswordFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener {

    private static String LOG_TAG = LoginActivity.class.getSimpleName();

    private LoginPresenterImpl presenter;

    public static final String LOGIN_FRAGMENT_TAG = "login";
    public static final String RESET_PASSWORD_FRAGMENT_TAG = "reset";
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
        hideKeyboard();
        presenter.login(email, password);
    }

    @Override
    public void onLoginFragmentForgotPasswordClicked() {
        showResetPasswordFragment();
    }

    @Override
    public void onLoginFragmentSignUpClicked() {
        showSignUpFragment();
    }

    @Override
    public void onResetPasswordFragmentResetClicked(String email) {
        presenter.resetPassword(email);
    }

    @Override
    public void onSignUpFragmentSignUpClicked(String name, String email, String type, String password) {
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
        // Ao pressionar Logout, a WorkoutsActivity não pode mais ser acessada através do back button
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

    private void showResetPasswordFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.replace_frame, ResetPasswordFragment.newInstance(), RESET_PASSWORD_FRAGMENT_TAG)
                .addToBackStack(RESET_PASSWORD_FRAGMENT_TAG)
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
        Toast.makeText(getApplicationContext(), FirebaseHelper.getInstance().getAuthUserEmail() + " registrado com sucesso" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpFailure() {
        Toast.makeText(getApplicationContext(), "Falha ao registrar ", Toast.LENGTH_SHORT).show();
    }


    // Login callbacks

    @Override
    public void onLoginSuccess() {
        Toast.makeText(getApplicationContext(), "Logado como " + FirebaseHelper.getInstance().getAuthUserEmail(), Toast.LENGTH_SHORT).show();
        showMainActivity();
    }

    @Override
    public void onLoginFailure() {
        Toast.makeText(getApplicationContext(), "Falha ao fazer login", Toast.LENGTH_SHORT).show();
    }


    // Password reset callbacks

    @Override
    public void onPasswordResetSuccess() {
        Toast.makeText(getApplicationContext(), "Verifique seu email para criar uma nova senha", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPasswordResetFailure() {
        Toast.makeText(getApplicationContext(), "Não há nenhuma conta registrada com o email informado", Toast.LENGTH_SHORT).show();
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