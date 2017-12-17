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
    private EditText mCompanyEditText;
    private EditText mPasswordEditText;
    private EditText mPasswordRepeatEditText;
    private Button mSignUpButton;
    private TextInputLayout mNameWrapper;
    private TextInputLayout mCompanyWrapper;
    private TextInputLayout mEmailWrapper;
    private TextInputLayout mPasswordWrapper;
    private Spinner mSpinner;

    private TextInputLayout mPasswordRepeatWrapper;
    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //region Constructors
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
    //endregion

    //region Fragment lifecycle

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

        // Clear error when name focus is true and validate name when focus is lost
        mNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mNameWrapper.setErrorEnabled(false);
                } else {
                    validateName();
                }
            }
        });

        // Clear error when company focus is true and validate name when focus is lost
        mCompanyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mCompanyWrapper.setErrorEnabled(false);
                } else {
                    validateCompany();
                }
            }
        });

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


        // Clear error when password repeat focus is true and validate password repeat when focus is lost
        mPasswordRepeatEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mPasswordRepeatWrapper.setErrorEnabled(false);
                } else {
                    validatePasswordRepeat();
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
        mNameWrapper = (TextInputLayout) view.findViewById(R.id.signup_nameWrapper);
        mCompanyWrapper = (TextInputLayout) view.findViewById(R.id.signup_companyWrapper);
        mEmailWrapper = (TextInputLayout) view.findViewById(R.id.signup_usernameWrapper);
        mPasswordWrapper = (TextInputLayout) view.findViewById(R.id.signup_passwordWrapper);
        mPasswordRepeatWrapper = (TextInputLayout) view.findViewById(R.id.signup_password2Wrapper);
        mNameEditText = (EditText) view.findViewById(R.id.signup_name_edit_text);
        mEmailEditText = (EditText) view.findViewById(R.id.signup_email_edit_text);
        mCompanyEditText = (EditText) view.findViewById(R.id.signup_company_edit_text);
        mSpinner = (Spinner)view.findViewById(R.id.user_type_spinner);
        mPasswordEditText = (EditText) view.findViewById(R.id.signup_password_edit_text);
        mPasswordRepeatEditText = (EditText) view.findViewById(R.id.signup_password2_edit_text);
        mSignUpButton = (Button) view.findViewById(R.id.signup_button);

        // Setup user type spinner
        List<String> spinnerArray =  new ArrayList<>();
        // TODO add constants (maybe in strings.xml)
        spinnerArray.add("customer");
        spinnerArray.add("acao");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        mSpinner.setAdapter(adapter);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpClicked(
                        mNameEditText.getText().toString(),
                        mCompanyEditText.getText().toString(),
                        mEmailEditText.getText().toString(),
                        mSpinner.getSelectedItem().toString(),
                        mPasswordEditText.getText().toString()
                );
            }
        });
    }

    public void onSignUpClicked(String name, String company, String email, String type, String password) {

        mNameWrapper.setErrorEnabled(false);
        mCompanyWrapper.setErrorEnabled(false);
        mEmailWrapper.setErrorEnabled(false);
        mPasswordWrapper.setErrorEnabled(false);
        mPasswordRepeatWrapper.setErrorEnabled(false);

        // TODO too many nested ifs?
        if (validateName() && validateCompany() && validateEmail() && validatePassword() && checkPasswordsMatch()) {
            if (mListener != null) {
                mListener.onSignUpFragmentSignUpClicked(name, company, email, type, password);
            }
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
        void onSignUpFragmentSignUpClicked(String name, String company, String email, String type, String password);
    }
    //endregion

    //region Data validation
    /**
     * Validate name and return error message if needed
     * @return email validity
     */
    private boolean validateName() {
        String name = mNameEditText.getText().toString();
        if (name.trim().isEmpty()) {
            mNameWrapper.setError("Nome em branco");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Validate company and return error message if needed
     * @return email validity
     */
    private boolean validateCompany() {
        String company = mCompanyEditText.getText().toString();
        if (company.trim().isEmpty()) {
            mCompanyWrapper.setError("Nome da empresa em branco");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Validate email and return error message if needed
     * @return email validity
     */
    private boolean validateEmail() {
        String email = mEmailEditText.getText().toString();
        if (!com.penseapp.acaocontabilidade.domain.Utilities.validateEmail(email)) {
            mEmailWrapper.setError("Endereço de email inválido");
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
        if (!com.penseapp.acaocontabilidade.domain.Utilities.validatePassword(password)) {
            mPasswordWrapper.setError("Senha deve ter no mínimo 6 caracteres");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Validate repeated password and return error message if needed
     * @return password validity
     */
    private boolean validatePasswordRepeat() {
        String password = mPasswordRepeatEditText.getText().toString();
        if (!com.penseapp.acaocontabilidade.domain.Utilities.validatePassword(password)) {
            mPasswordRepeatWrapper.setError("Senha deve ter no mínimo 6 caracteres");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check is password entered on both fields match
     * @return password validity
     */
    private boolean checkPasswordsMatch() {
        String password = mPasswordEditText.getText().toString();
        String passwordRepeat = mPasswordRepeatEditText.getText().toString();
        if (!password.equals(passwordRepeat)) {
            mPasswordWrapper.setError("Senhas devem ser iguais");
            mPasswordRepeatWrapper.setError("Senhas devem ser iguais");
            return false;
        } else {
            return true;
        }
    }
    //endregion
}
