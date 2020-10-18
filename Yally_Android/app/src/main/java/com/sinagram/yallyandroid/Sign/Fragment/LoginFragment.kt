package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Data.LoginRequest
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.LoginViewModel
import com.sinagram.yallyandroid.Util.TextWatcherImpl
import kotlinx.android.synthetic.main.signinup_layout.*
import kotlinx.android.synthetic.main.signinup_layout.view.*

class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val loginRequest = LoginRequest()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signinup_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            signinup_email_editText.addTextChangedListener(
                createTextWatcher(signinup_email_inputLayout)
            )
            signinup_password_editText.addTextChangedListener(
                createTextWatcher(signinup_password_inputLayout)
            )
            signinup_forgot_textView.setOnClickListener {
                (activity as SignActivity).replaceFragment(ResetPasswordFragment())
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val signActivity = activity as SignActivity

        signActivity.findViewById<TextView>(R.id.sign_title_textView).text =
            getString(R.string.login)

        loginViewModel.errorSignLiveData.observe(viewLifecycleOwner, {
            showErrorMessage()
        })

        loginViewModel.loginSuccessLiveData.observe(viewLifecycleOwner, {
            signActivity.moveToMain()
            signActivity.finish()
        })
    }

    private fun createTextWatcher(textInputLayout: TextInputLayout): TextWatcher {
        return object : TextWatcherImpl() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayout.error = null
            }
            override fun afterTextChanged(s: Editable?) {
                when (textInputLayout) {
                    signinup_password_inputLayout -> loginRequest.password = s.toString()
                    signinup_email_inputLayout -> loginRequest.email = s.toString()
                }
                activeButton(signinup_doSign_button)
            }
        }
    }

    private fun activeButton(button: Button) {
        button.apply {
            if (loginRequest.scanEnteredLoginInformation()) {
                setBackgroundResource(R.drawable.button_gradient)
                setOnClickListener { loginViewModel.mappingLoginInfo(loginRequest) }
            } else {
                setBackgroundResource(R.drawable.button_bright_gray)
                setOnClickListener(null)
            }
        }
    }

    private fun showErrorMessage() {
        signinup_email_inputLayout.error = getString(R.string.account_does_not_exits)
        signinup_password_inputLayout.error = getString(R.string.password_not_correct)
    }
}