package com.penseapp.acaocontabilidade.login.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.penseapp.acaocontabilidade.R
import com.penseapp.acaocontabilidade.domain.Utilities

//region Constructors
class LoginFragment : Fragment() {

    // GUI
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mEmailEditText: EditText
    private lateinit var mPasswordEditText: EditText
    private lateinit var mEmailWrapper: TextInputLayout
    private lateinit var mPasswordWrapper: TextInputLayout

    private var mListener: OnFragmentInteractionListener? = null
    //endregion

    //region Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createUI(view)

        // Clear error when email focus is true and validate email when focus is lost
        mEmailEditText.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                mEmailWrapper.isErrorEnabled = false
            } else {
                validateEmail()
            }
        }

        // Clear error when password focus is true and validate password when focus is lost
        mPasswordEditText.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                mPasswordWrapper.isErrorEnabled = false
            } else {
                validatePassword()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
    //endregion

    //region GUI creation and interaction
    private fun createUI(view: View) {
        mProgressBar = view.findViewById(R.id.progress_bar)
        mEmailWrapper = view.findViewById(R.id.usernameWrapper)
        mPasswordWrapper = view.findViewById(R.id.passwordWrapper)
        mEmailEditText = view.findViewById(R.id.login_email_edit_text)
        mPasswordEditText = view.findViewById(R.id.login_password_edit_text)

        val mLoginButton = view.findViewById<Button>(R.id.login_button)
        mLoginButton.setOnClickListener {
            onLoginClicked(mEmailEditText.text.toString(), mPasswordEditText.text.toString())
        }

        val mResetPasswordText = view.findViewById<TextView>(R.id.forgot_password_text)
        mResetPasswordText.setOnClickListener { onForgotPasswordClicked() }

        val mSignUpText = view.findViewById<TextView>(R.id.signup_text)
        mSignUpText.setOnClickListener { onSignUpClicked() }
    }

    private fun onLoginClicked(email: String, password: String) {
        mEmailWrapper.isErrorEnabled = false
        mPasswordWrapper.isErrorEnabled = false

        if (validateEmail() && validatePassword()) {
            if (mListener != null) {
                mListener!!.onLoginFragmentLoginClicked(email, password)
            }
        }
    }

    private fun onForgotPasswordClicked() {
        if (mListener != null) {
            mListener!!.onLoginFragmentForgotPasswordClicked()
        }
    }

    private fun onSignUpClicked() {
        if (context != null) {
            Utilities.goToAcaoNewCustomerWebsite(context!!)
        }
    }
    //endregion

    fun spinProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    fun stopProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    //region Communication between fragment and parent activity
    interface OnFragmentInteractionListener {
        fun onLoginFragmentLoginClicked(email: String, password: String)

        fun onLoginFragmentForgotPasswordClicked()

        fun onLoginFragmentSignUpClicked()
    }
    //endregion

    //region Data validation
    /**
     * Validate email and return error message if needed
     * @return email validity
     */
    private fun validateEmail(): Boolean {
        val email = mEmailEditText.text.toString()
        return if (!Utilities.validateEmail(email)) {
            mEmailWrapper.error = getString(R.string.login_invalid_email)
            false
        } else {
            true
        }
    }

    /**
     * Validate password and return error message if needed
     * @return password validity
     */
    private fun validatePassword(): Boolean {
        val password = mPasswordEditText.text.toString()
        return if (!Utilities.validatePassword(password)) {
            mPasswordWrapper.error = getString(R.string.login_password_too_short_error)
            false
        } else {
            true
        }
    }

    companion object {

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
    //endregion
}
