package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.LoginViewModel
import kotlinx.android.synthetic.main.signinup_layout.*
import kotlinx.android.synthetic.main.signinup_layout.view.*

class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by viewModels()
    var email = ""
    var password = ""

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

        loginViewModel.errorMessageLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            showErrorMessage()
        })

        loginViewModel.loginSuccessLiveData.observe(viewLifecycleOwner, {
            signActivity.moveToMain()
            signActivity.finish()
        })
    }

    private fun createTextWatcher(textInputLayout: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textInputLayout.error = null
            }

            override fun afterTextChanged(p0: Editable?) {
                when (textInputLayout) {
                    signinup_password_inputLayout -> password = p0.toString()
                    signinup_email_inputLayout -> email = p0.toString()
                }
                activeButton(signinup_doSign_button)
            }
        }
    }

    private fun activeButton(button: Button) {
        button.apply {
            if (email.length in 1..30 && password.length >= 8 && password.isNotEmpty()) {
                setBackgroundResource(R.drawable.button_gradient)
                setOnClickListener { loginViewModel.mappingLoginInfo(email, password) }
            } else {
                setBackgroundResource(R.drawable.button_bright_gray)
                setOnClickListener(null)
            }
        }
    }

    private fun showErrorMessage() {
        signinup_email_inputLayout.error = "존재하지 않는 계정입니다"
        signinup_password_inputLayout.error = "비밀번호가 올바르지 않습니다"
    }
}