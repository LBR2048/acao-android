package com.penseapp.acaocontabilidade.login.view.fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.penseapp.acaocontabilidade.login.Utilities;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResetPasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // GUI
    private ProgressBar mProgressBar;
    private EditText mEmailEditText;
    private Button mLoginButton;
    private TextView mSignUpText;
    private TextInputLayout mEmailWrapper;
    private TextInputLayout mPasswordWrapper;

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    public static ResetPasswordFragment newInstance() {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    // Fragment lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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


    // GUI creation and interaction

    private void createUI(View view) {
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mEmailWrapper = (TextInputLayout) view.findViewById(R.id.usernameWrapper);
        mPasswordWrapper = (TextInputLayout) view.findViewById(R.id.passwordWrapper);
        mEmailEditText = (EditText) view.findViewById(R.id.login_email_edit_text);
        mLoginButton = (Button) view.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResetClicked(mEmailEditText.getText().toString());
            }
        });
    }

    public void onResetClicked(String email) {

        mEmailWrapper.setErrorEnabled(false);

        if (validateEmail()) {
            if (mListener != null) {
                mListener.onResetPasswordFragmentResetClicked(email);
            }
        }
    }


    public void spinProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }


    // Communication between fragment and parent activity
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
        void onResetPasswordFragmentResetClicked(String email);
    }


    /**
     * Validate email and return error message if needed
     * @return email validity
     */
    private boolean validateEmail() {
        String email = mEmailEditText.getText().toString();
        if (!Utilities.validateEmail(email)) {
            mEmailWrapper.setError("Endereço de email inválido");
            return false;
        } else {
            return true;
        }
    }
}