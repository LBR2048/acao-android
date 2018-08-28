package com.penseapp.acaocontabilidade.login.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.domain.Utilities;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // GUI
    private ProgressBar mProgressBar;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private TextInputLayout mEmailWrapper;
    private TextInputLayout mPasswordWrapper;

    private OnFragmentInteractionListener mListener;

    //region Constructors
    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region Fragment lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        createUI(view);

        // Clear error when email focus is true and validate email when focus is lost
        mEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mEmailWrapper.setErrorEnabled(false);
                } else {
                    validateEmail();
                }
            }
        });

        // Clear error when password focus is true and validate password when focus is lost
        mPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mPasswordWrapper.setErrorEnabled(false);
                } else {
                    validatePassword();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    //endregion

    //region GUI creation and interaction
    private void createUI(View view) {
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mEmailWrapper = (TextInputLayout) view.findViewById(R.id.usernameWrapper);
        mPasswordWrapper = (TextInputLayout) view.findViewById(R.id.passwordWrapper);
        mEmailEditText = (EditText) view.findViewById(R.id.login_email_edit_text);
        mPasswordEditText = (EditText) view.findViewById(R.id.login_password_edit_text);
        Button mLoginButton = (Button) view.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClicked(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString());
            }
        });
        TextView mResetPasswordText = (TextView) view.findViewById(R.id.forgot_password_text);
        mResetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onForgotPasswordClicked();
            }
        });
        TextView mSignUpText = (TextView) view.findViewById(R.id.signup_text);
        mSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpClicked();
            }
        });
    }

    private void onLoginClicked(String email, String password) {
        mEmailWrapper.setErrorEnabled(false);
        mPasswordWrapper.setErrorEnabled(false);

        if (validateEmail() && validatePassword()) {
            if (mListener != null) {
                mListener.onLoginFragmentLoginClicked(email, password);
            }
        }
    }

    private void onForgotPasswordClicked() {
        if (mListener != null) {
                mListener.onLoginFragmentForgotPasswordClicked();
            }
    }

    private void onSignUpClicked() {
        if (getContext() != null){
            Utilities.goToAcaoNewCustomerWebsite(getContext());
        }
    }
    //endregion

    public void spinProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    //region Communication between fragment and parent activity
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onLoginFragmentLoginClicked(String email, String password);

        void onLoginFragmentForgotPasswordClicked();

        void onLoginFragmentSignUpClicked();
    }
    //endregion

    //region Data validation
    /**
     * Validate email and return error message if needed
     * @return email validity
     */
    private boolean validateEmail() {
        String email = mEmailEditText.getText().toString();
        if (!Utilities.validateEmail(email)) {
            mEmailWrapper.setError(getString(R.string.login_invalid_email));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Validate password and return error message if needed
     * @return password validity
     */
    private boolean validatePassword() {
        String password = mPasswordEditText.getText().toString();
        if (!Utilities.validatePassword(password)) {
            mPasswordWrapper.setError(getString(R.string.login_password_too_short_error));
            return false;
        } else {
            return true;
        }
    }
    //endregion
}
