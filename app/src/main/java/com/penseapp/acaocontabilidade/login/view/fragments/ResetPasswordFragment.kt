package com.penseapp.acaocontabilidade.login.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.penseapp.acaocontabilidade.R
import com.penseapp.acaocontabilidade.domain.Utilities
import kotlinx.android.synthetic.main.fragment_reset_password.*

//region Constructors
class ResetPasswordFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    //region Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
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
        loginButton.setOnClickListener { onResetClicked(emailEditText.text.toString()) }
    }

    private fun onResetClicked(email: String) {

        emailWrapper.isErrorEnabled = false

        if (validateEmail()) {
            if (mListener != null) {
                mListener!!.onResetPasswordFragmentResetClicked(email)
            }
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
        fun onResetPasswordFragmentResetClicked(email: String)
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
