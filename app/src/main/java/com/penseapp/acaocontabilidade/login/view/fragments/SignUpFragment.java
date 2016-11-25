package com.penseapp.acaocontabilidade.login.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.login.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // GUI
    private ProgressBar mProgressBar;
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mPassword2EditText;
    private Button mSignUpButton;
    private TextInputLayout mNameWrapper;
    private TextInputLayout mUsernameWrapper;
    private TextInputLayout mPasswordWrapper;
    private Spinner mSpinner;

    private TextInputLayout mPassword2Wrapper;
    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        createUI(view);
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
        mNameWrapper = (TextInputLayout) view.findViewById(R.id.signup_nameWrapper);
        mUsernameWrapper = (TextInputLayout) view.findViewById(R.id.signup_usernameWrapper);
        mPasswordWrapper = (TextInputLayout) view.findViewById(R.id.signup_passwordWrapper);
        mPassword2Wrapper = (TextInputLayout) view.findViewById(R.id.signup_password2Wrapper);
        mNameEditText = (EditText) view.findViewById(R.id.signup_name_edit_text);
        mEmailEditText = (EditText) view.findViewById(R.id.signup_email_edit_text);
        mSpinner = (Spinner)view.findViewById(R.id.user_type_spinner);
        mPasswordEditText = (EditText) view.findViewById(R.id.signup_password_edit_text);
        mPassword2EditText = (EditText) view.findViewById(R.id.signup_password2_edit_text);
        mSignUpButton = (Button) view.findViewById(R.id.signup_button);

        // Setup user type spinner
        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("acao");
        spinnerArray.add("customer");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        mSpinner.setAdapter(adapter);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpClicked(
                        mNameEditText.getText().toString(),
                        mEmailEditText.getText().toString(),
                        mSpinner.getSelectedItem().toString(),
                        mPasswordEditText.getText().toString(), mPassword2EditText.getText().toString());
            }
        });
    }

    public void onSignUpClicked(String name, String email, String type, String password, String password2) {

        mNameWrapper.setErrorEnabled(false);
        mUsernameWrapper.setErrorEnabled(false);
        mPasswordWrapper.setErrorEnabled(false);
        mPassword2Wrapper.setErrorEnabled(false);

        // TODO too many nested ifs?
        if (name.trim().isEmpty())
            mNameWrapper.setError("Name cannot be empty");
        else if (!Utilities.validateEmail(email))
            mUsernameWrapper.setError("Not a valid email address");
        else if (!Utilities.validatePassword(password))
            mPasswordWrapper.setError("Not a valid password");
        else if (!Utilities.validatePassword(password2))
            mPassword2Wrapper.setError("Not a valid password");
        else if (!password.equals(password2)) {
            mPasswordWrapper.setError("Passwords must match");
            mPassword2Wrapper.setError("Passwords must match");
        }
        else {
//            hideKeyboard();
//            presenter.login(email, password);
//        }
            if (mListener != null) {
                mListener.onSignUpFragmentSignUpClicked(name, email, type, password);
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
        void onSignUpFragmentSignUpClicked(String name, String email, String type, String password);
    }

}
