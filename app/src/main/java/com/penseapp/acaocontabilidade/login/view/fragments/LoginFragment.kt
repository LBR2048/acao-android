package com.penseapp.acaocontabilidade.login.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.penseapp.acaocontabilidade.R
import com.penseapp.acaocontabilidade.domain.Utilities
import kotlinx.android.synthetic.main.fragment_login.*

//region Constructors
class LoginFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    //region Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createUI(view)

        // Clear error when email focus is true and validate email when focus is lost
        emailEditText.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                emailWrapper.isErrorEnabled = false
            } else {
                validateEmail()
            }
        }

        // Clear error when password focus is true and validate password when focus is lost
        passwordEditText.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                passwordWrapper.isErrorEnabled = false
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

        loginButton.setOnClickListener {
            onLoginClicked(emailEditText.text.toString(), passwordEditText.text.toString())
        }

        resetPasswordText.setOnClickListener { onForgotPasswordClicked() }

        signUpText.setOnClickListener { onSignUpClicked() }
    }

    private fun onLoginClicked(email: String, password: String) {
        emailWrapper.isErrorEnabled = false
        passwordWrapper.isErrorEnabled = false

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
        progressBar.visibility = View.VISIBLE
    }

    fun stopProgressBar() {
        progressBar.visibility = View.GONE
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
        val email = emailEditText.text.toString()
        return if (!Utilities.validateEmail(email)) {
            emailWrapper.error = getString(R.string.login_invalid_email)
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
        val password = passwordEditText.text.toString()
        return if (!Utilities.validatePassword(password)) {
            passwordWrapper.error = getString(R.string.login_password_too_short_error)
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
