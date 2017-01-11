package com.penseapp.acaocontabilidade.login.interactor;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.login.presenter.LoginPresenter;

/**
 * Created by unity on 21/11/2016.
 */
//// TODO: 26/02/2016 add emoji to users and implement them into mMessages
public class LoginInteractorImpl implements LoginInteractor {

    private static String LOG_TAG = LoginInteractorImpl.class.getSimpleName();

    private final LoginPresenter presenter;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public LoginInteractorImpl(LoginPresenter presenter) {
        this.presenter = presenter;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signUp(final String name, String email, final String type, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String email = authResult.getUser().getEmail();
                        String userId = authResult.getUser().getUid();
                        FirebaseHelper.getInstance().getUsersReference().child(userId).child("name").setValue(name);
                        FirebaseHelper.getInstance().getUsersReference().child(userId).child("email").setValue(email);
                        FirebaseHelper.getInstance().getUsersReference().child(userId).child("key").setValue(userId);
                        FirebaseHelper.getInstance().getUsersReference().child(userId).child("type").setValue(type);

                        presenter.onSignUpSuccess(email, userId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        presenter.onSignUpFailure();
                    }
                });
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(LOG_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                        // ...
//                    }
//                });
    }

    @Override
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String email = authResult.getUser().getEmail();
                        String userId = authResult.getUser().getUid();
                        presenter.onLoginSuccess(email, userId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        presenter.onLoginFailure();
                    }
                });
    }

    @Override
    public void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            presenter.onPasswordResetSuccess();
                        } else {
                            presenter.onPasswordResetFailure();
                        }
                    }
                });
    }

    @Override
    public void getCurrentUser() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    presenter.onUserLoggedIn();
                } else {
                    // User is signed out
                    presenter.onUserLoggedOut();
                }
                // ...
            }
        };
    }

    @Override
    public void addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void removeAuthStateListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
