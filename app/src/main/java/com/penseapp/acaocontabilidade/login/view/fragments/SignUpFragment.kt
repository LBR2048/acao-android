package com.penseapp.acaocontabilidade.login.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import com.penseapp.acaocontabilidade.R
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.*

//endregion

//region Constructors
class SignUpFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    //region Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createUI(view)

        // Clear error when name focus is true and validate name when focus is lost
        nameEditText.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                nameWrapper.isErrorEnabled = false
            } else {
                validateName()
            }
        }

        // Clear error when company focus is true and validate name when focus is lost
        companyEditText.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                companyWrapper.isErrorEnabled = false
            } else {
                validateCompany()
            }
        }

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


        // Clear error when password repeat focus is true and validate password repeat when focus is lost
        passwordRepeatEditText.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                passwordRepeatWrapper.isErrorEnabled = false
            } else {
                validatePasswordRepeat()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
    //endregion

    //region GUI creation and interaction

    private fun createUI(view: View) {
        val signUpButton = view.findViewById<Button>(R.id.signUpButton)

        // Setup user type spinner
        val spinnerArray = ArrayList<String>()
        // TODO add constants (maybe in strings.xml)
        spinnerArray.add("customer")
        spinnerArray.add("acao")
        if (context != null) {
            val adapter = ArrayAdapter(
                    context!!, android.R.layout.simple_spinner_item, spinnerArray)
            spinner.adapter = adapter
        }

        signUpButton.setOnClickListener {
            onSignUpClicked(
                    nameEditText.text.toString(),
                    companyEditText.text.toString(),
                    emailEditText.text.toString(),
                    spinner.selectedItem.toString(),
                    passwordEditText.text.toString()
            )
        }
    }

    private fun onSignUpClicked(name: String, company: String, email: String, type: String, password: String) {

        nameWrapper.isErrorEnabled = false
        companyWrapper.isErrorEnabled = false
        emailWrapper.isErrorEnabled = false
        passwordWrapper.isErrorEnabled = false
        passwordRepeatWrapper.isErrorEnabled = false

        // TODO too many nested ifs?
        if (validateName() && validateCompany() && validateEmail() && validatePassword() && checkPasswordsMatch()) {
            if (mListener != null) {
                mListener!!.onSignUpFragmentSignUpClicked(name, company, email, type, password)
            }
        }
    }
    //endregion

    fun spinProgressBar() {
//        progressBar.visibility = View.VISIBLE
    }

    fun stopProgressBar() {
//        progressBar.visibility = View.GONE
    }


    //region Communication between fragment and parent activity
    interface OnFragmentInteractionListener {
        fun onSignUpFragmentSignUpClicked(name: String, company: String, email: String, type: String, password: String)
    }
    //endregion

    //region Data validation
    /**
     * Validate name and return error message if needed
     * @return email validity
     */
    private fun validateName(): Boolean {
        val name = nameEditText.text.toString()
        return if (name.trim { it <= ' ' }.isEmpty()) {
            nameWrapper.error = getString(R.string.login_register_blank_name_error)
            false
        } else {
            true
        }
    }

    /**
     * Validate company and return error message if needed
     * @return email validity
     */
    private fun validateCompany(): Boolean {
        val company = companyEditText.text.toString()
        return if (company.trim { it <= ' ' }.isEmpty()) {
            companyWrapper.error = getString(R.string.login_register_blank_company_name_error)
            false
        } else {
            true
        }
    }

    /**
     * Validate email and return error message if needed
     * @return email validity
     */
    private fun validateEmail(): Boolean {
        val email = emailEditText.text.toString()
        return if (!com.penseapp.acaocontabilidade.domain.Utilities.validateEmail(email)) {
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
        return if (!com.penseapp.acaocontabilidade.domain.Utilities.validatePassword(password)) {
            passwordWrapper.error = getString(R.string.login_password_too_short_error)
            false
        } else {
            true
        }
    }

    /**
     * Validate repeated password and return error message if needed
     * @return password validity
     */
    private fun validatePasswordRepeat(): Boolean {
        val password = passwordRepeatEditText.text.toString()
        return if (!com.penseapp.acaocontabilidade.domain.Utilities.validatePassword(password)) {
            passwordRepeatWrapper.error = getString(R.string.login_password_too_short_error)
            false
        } else {
            true
        }
    }

    /**
     * Check is password entered on both fields match
     * @return password validity
     */
    private fun checkPasswordsMatch(): Boolean {
        val password = passwordEditText.text.toString()
        val passwordRepeat = passwordRepeatEditText.text.toString()
        return if (password != passwordRepeat) {
            passwordWrapper.error = getString(R.string.login_register_passwords_must_match)
            passwordRepeatWrapper.error = getString(R.string.login_register_passwords_must_match)
            false
        } else {
            true
        }
    }

    companion object {

        fun newInstance(): SignUpFragment {
            return SignUpFragment()
        }
    }
    //endregion
}// Required empty public constructor
