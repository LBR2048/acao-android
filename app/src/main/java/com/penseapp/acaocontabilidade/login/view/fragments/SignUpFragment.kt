package com.penseapp.acaocontabilidade.login.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.penseapp.acaocontabilidade.R
import java.util.*

//endregion

//region Constructors
class SignUpFragment : Fragment() {

    //region GUI
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mNameEditText: EditText
    private lateinit var mEmailEditText: EditText
    private lateinit var mCompanyEditText: EditText
    private lateinit var mPasswordEditText: EditText
    private lateinit var mPasswordRepeatEditText: EditText
    private lateinit var mNameWrapper: TextInputLayout
    private lateinit var mCompanyWrapper: TextInputLayout
    private lateinit var mEmailWrapper: TextInputLayout
    private lateinit var mPasswordWrapper: TextInputLayout
    private lateinit var mPasswordRepeatWrapper: TextInputLayout
    private lateinit var mSpinner: Spinner

    private var mListener: OnFragmentInteractionListener? = null
    //endregion

    //region Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createUI(view)

        // Clear error when name focus is true and validate name when focus is lost
        mNameEditText.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                mNameWrapper.isErrorEnabled = false
            } else {
                validateName()
            }
        }

        // Clear error when company focus is true and validate name when focus is lost
        mCompanyEditText.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                mCompanyWrapper.isErrorEnabled = false
            } else {
                validateCompany()
            }
        }

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


        // Clear error when password repeat focus is true and validate password repeat when focus is lost
        mPasswordRepeatEditText.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                mPasswordRepeatWrapper.isErrorEnabled = false
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
        mProgressBar = view.findViewById(R.id.progress_bar)
        mNameWrapper = view.findViewById(R.id.signup_nameWrapper)
        mCompanyWrapper = view.findViewById(R.id.signup_companyWrapper)
        mEmailWrapper = view.findViewById(R.id.signup_usernameWrapper)
        mPasswordWrapper = view.findViewById(R.id.signup_passwordWrapper)
        mPasswordRepeatWrapper = view.findViewById(R.id.signup_password2Wrapper)
        mNameEditText = view.findViewById(R.id.signup_name_edit_text)
        mEmailEditText = view.findViewById(R.id.signup_email_edit_text)
        mCompanyEditText = view.findViewById(R.id.signup_company_edit_text)
        mSpinner = view.findViewById(R.id.user_type_spinner)
        mPasswordEditText = view.findViewById(R.id.signup_password_edit_text)
        mPasswordRepeatEditText = view.findViewById(R.id.signup_password2_edit_text)
        val mSignUpButton = view.findViewById<Button>(R.id.signup_button)

        // Setup user type spinner
        val spinnerArray = ArrayList<String>()
        // TODO add constants (maybe in strings.xml)
        spinnerArray.add("customer")
        spinnerArray.add("acao")
        if (context != null) {
            val adapter = ArrayAdapter(
                    context!!, android.R.layout.simple_spinner_item, spinnerArray)
            mSpinner.adapter = adapter
        }

        mSignUpButton.setOnClickListener {
            onSignUpClicked(
                    mNameEditText.text.toString(),
                    mCompanyEditText.text.toString(),
                    mEmailEditText.text.toString(),
                    mSpinner.selectedItem.toString(),
                    mPasswordEditText.text.toString()
            )
        }
    }

    private fun onSignUpClicked(name: String, company: String, email: String, type: String, password: String) {

        mNameWrapper.isErrorEnabled = false
        mCompanyWrapper.isErrorEnabled = false
        mEmailWrapper.isErrorEnabled = false
        mPasswordWrapper.isErrorEnabled = false
        mPasswordRepeatWrapper.isErrorEnabled = false

        // TODO too many nested ifs?
        if (validateName() && validateCompany() && validateEmail() && validatePassword() && checkPasswordsMatch()) {
            if (mListener != null) {
                mListener!!.onSignUpFragmentSignUpClicked(name, company, email, type, password)
            }
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
        fun onSignUpFragmentSignUpClicked(name: String, company: String, email: String, type: String, password: String)
    }
    //endregion

    //region Data validation
    /**
     * Validate name and return error message if needed
     * @return email validity
     */
    private fun validateName(): Boolean {
        val name = mNameEditText.text.toString()
        return if (name.trim { it <= ' ' }.isEmpty()) {
            mNameWrapper.error = getString(R.string.login_register_blank_name_error)
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
        val company = mCompanyEditText.text.toString()
        return if (company.trim { it <= ' ' }.isEmpty()) {
            mCompanyWrapper.error = getString(R.string.login_register_blank_company_name_error)
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
        val email = mEmailEditText.text.toString()
        return if (!com.penseapp.acaocontabilidade.domain.Utilities.validateEmail(email)) {
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
        return if (!com.penseapp.acaocontabilidade.domain.Utilities.validatePassword(password)) {
            mPasswordWrapper.error = getString(R.string.login_password_too_short_error)
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
        val password = mPasswordRepeatEditText.text.toString()
        return if (!com.penseapp.acaocontabilidade.domain.Utilities.validatePassword(password)) {
            mPasswordRepeatWrapper.error = getString(R.string.login_password_too_short_error)
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
        val password = mPasswordEditText.text.toString()
        val passwordRepeat = mPasswordRepeatEditText.text.toString()
        return if (password != passwordRepeat) {
            mPasswordWrapper.error = getString(R.string.login_register_passwords_must_match)
            mPasswordRepeatWrapper.error = getString(R.string.login_register_passwords_must_match)
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
