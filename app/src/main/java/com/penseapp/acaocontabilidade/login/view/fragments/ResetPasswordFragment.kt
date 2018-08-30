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
class ResetPasswordFragment : Fragment() {

    // GUI
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mEmailEditText: EditText
    private lateinit var mLoginButton: Button
    private lateinit var mSignUpText: TextView
    private lateinit var mEmailWrapper: TextInputLayout
    private lateinit var mPasswordWrapper: TextInputLayout

    private var mListener: OnFragmentInteractionListener? = null
    //endregion

    //region Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
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
        mEmailWrapper = view.findViewById(R.id.usernameWrapper)
        mPasswordWrapper = view.findViewById(R.id.passwordWrapper)
        mEmailEditText = view.findViewById(R.id.login_email_edit_text)
        mLoginButton = view.findViewById(R.id.login_button)
        mLoginButton.setOnClickListener { onResetClicked(mEmailEditText.text.toString()) }
    }

    private fun onResetClicked(email: String) {

        mEmailWrapper.isErrorEnabled = false

        if (validateEmail()) {
            if (mListener != null) {
                mListener!!.onResetPasswordFragmentResetClicked(email)
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
        fun onResetPasswordFragmentResetClicked(email: String)
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

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        fun newInstance(): ResetPasswordFragment {
            return ResetPasswordFragment()
        }
    }
    //endregion
}// Required empty public constructor
