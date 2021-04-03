package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Data.LoginRequest
import com.sinagram.yallyandroid.Sign.Data.PasswordProcess
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.LoginViewModel
import com.sinagram.yallyandroid.Util.TextWatcherImpl
import kotlinx.android.synthetic.main.signinup_layout.*
import kotlinx.android.synthetic.main.signinup_layout.signinup_email_inputLayout
import kotlinx.android.synthetic.main.signinup_layout.signinup_password_inputLayout
import kotlinx.android.synthetic.main.signinup_layout.view.*

class ResetPasswordFragment : Fragment() {
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
            changeEmailPage()

            signinup_email_editText.addTextChangedListener(
                createTextWatcher(signinup_email_inputLayout)
            )
            signinup_password_editText.addTextChangedListener(
                createTextWatcher(signinup_password_inputLayout)
            )
            signinup_comfirm_password_editText.addTextChangedListener(
                createTextWatcher(signinup_comfirm_password_inputLayout)
            )

            signinup_authCode_pinView.addTextChangedListener(object : TextWatcherImpl() {
                override fun afterTextChanged(s: Editable?) {
                    loginRequest.pinCode = s.toString()
                    signinup_pinError_textView.visibility = View.GONE
                    activeButton(signinup_doSign_button)
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val signActivity = activity as SignActivity

        loginViewModel.errorSignLiveData.observe(viewLifecycleOwner, {
            showErrorMessage(it)
        })

        loginViewModel.loginSuccessLiveData.observe(viewLifecycleOwner, {
            when (it) {
                PasswordProcess.Email -> {
                    loginRequest.addDataInHashMap("email", loginRequest.email)
                    loginRequest.email = ""
                    changeCodePage()
                }
                PasswordProcess.Code -> {
                    loginRequest.addDataInHashMap("code", loginRequest.pinCode)
                    loginRequest.pinCode = ""
                    changePasswordPage()
                }
                PasswordProcess.Password -> {
                    signActivity.moveToMain()
                    signActivity.finish()
                }
                else -> {
                }
            }
        })
    }

    private fun changeEmailPage() {
        signinup_password_inputLayout.visibility = View.GONE
        signinup_title_textView.text = getString(R.string.password_initialization)
        signinup_subtitle_textView.text = getString(R.string.input_email)
        signinup_doSign_button.text = getString(R.string.next)
        signinup_forgot_textView.visibility = View.GONE
    }

    private fun changeCodePage() {
        signinup_email_inputLayout.visibility = View.GONE
        signinup_authCode_pinView.visibility = View.VISIBLE
        signinup_subtitle_textView.text = getString(R.string.input_reset_code)
        signinup_doSign_button.apply {
            text = getString(R.string.confirm)
            setBackgroundResource(R.drawable.button_bright_gray)
            setOnClickListener(null)
        }
    }

    private fun changePasswordPage() {
        signinup_authCode_pinView.visibility = View.GONE
        signinup_password_inputLayout.visibility = View.VISIBLE
        signinup_comfirm_password_inputLayout.visibility = View.VISIBLE
        signinup_subtitle_textView.text = getString(R.string.input_new_password)
        signinup_password_editText.hint = getString(R.string.new_password)
        signinup_doSign_button.apply {
            text = getString(R.string.initialization)
            setBackgroundResource(R.drawable.button_bright_gray)
            setOnClickListener(null)
        }
    }

    private fun createTextWatcher(textInputLayout: TextInputLayout): TextWatcher {
        return object : TextWatcherImpl() {
            override fun afterTextChanged(s: Editable?) {
                textInputLayout.error = null
                when (textInputLayout) {
                    signinup_email_inputLayout -> loginRequest.email = s.toString()
                    signinup_password_inputLayout -> loginRequest.password = s.toString()
                    signinup_comfirm_password_inputLayout -> loginRequest.confirm = s.toString()
                }
                activeButton(signinup_doSign_button)
            }
        }
    }

    private fun activeButton(button: Button) {
        button.apply {
            setBackgroundResource(R.drawable.button_gradient)

            when {
                loginRequest.scanEnteredEmail() -> {
                    button.setOnClickListener {
                        loginViewModel.sendResetCode(loginRequest.email)
                    }
                }
                loginRequest.scanEnteredPinCode() -> {
                    button.setOnClickListener {
                        loginViewModel.checkResetCode()
                    }
                }
                loginRequest.scanEnteredPassword() -> {
                    button.setOnClickListener {
                        loginRequest.addDataInHashMap("password", loginRequest.password)
                        loginViewModel.sendResetPassword(loginRequest.hashMap)
                    }
                }
                else -> {
                    setBackgroundResource(R.drawable.button_bright_gray)
                    setOnClickListener(null)
                }
            }
        }
    }

    private fun showErrorMessage(currentProcess: PasswordProcess) {
        when (currentProcess) {
            PasswordProcess.Email -> {
                signinup_email_inputLayout.error = getString(R.string.account_does_not_exits)
            }
            PasswordProcess.Code -> {
                signinup_pinError_textView.visibility = View.VISIBLE
            }
            PasswordProcess.Password -> {
                signinup_password_inputLayout.error =
                    getString(R.string.password_format_not_correct)
                signinup_comfirm_password_inputLayout.error =
                    getString(R.string.password_not_equals)
            }
            else -> {
            }
        }
    }
}