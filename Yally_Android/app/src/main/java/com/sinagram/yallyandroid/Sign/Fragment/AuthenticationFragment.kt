package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Data.SignProcess
import com.sinagram.yallyandroid.Sign.Data.SignUpRequest
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.SignUpViewModel
import com.sinagram.yallyandroid.Util.TextWatcherImpl
import kotlinx.android.synthetic.main.signinup_layout.*
import kotlinx.android.synthetic.main.signinup_layout.view.*

class AuthenticationFragment : Fragment() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    private var pinCode = ""
    private var signUpFragment = SignUpFragment()
    private val signUpRequest = SignUpRequest()

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
            signinup_email_editText.addTextChangedListener(object : TextWatcherImpl() {
                override fun afterTextChanged(s: Editable?) {
                    signinup_email_inputLayout.error = null
                    signUpRequest.email = s.toString()
                    activeButton(signinup_doSign_button)
                }
            })
            signinup_authCode_pinView.addTextChangedListener(object : TextWatcherImpl() {
                override fun afterTextChanged(s: Editable?) {
                    signinup_pinError_textView.text = null
                    pinCode = s.toString()
                    activeButton(signinup_doSign_button)
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val signActivity = activity as SignActivity

        signActivity.findViewById<TextView>(R.id.sign_title_textView).text =
            getString(R.string.sign_up)

        signUpViewModel.errorSignLiveData.observe(viewLifecycleOwner, {
            showErrorMessage(it)
        })

        signUpViewModel.signUpSuccessLiveData.observe(viewLifecycleOwner, {
            when (it) {
                SignProcess.GetCode -> {
                    changeCodePage()
                }
                SignProcess.CheckCode -> {
                    signActivity.replaceFragment(signUpFragment)
                }
                else -> {
                }
            }
        })
    }

    private fun changeEmailPage() {
        signinup_title_textView.text = getString(R.string.welcome_first_visit)
        signinup_subtitle_textView.text = getString(R.string.start_sign_up)
        signinup_password_inputLayout.visibility = View.GONE
        signinup_doSign_button.text = getString(R.string.next)
        signinup_forgot_textView.visibility = View.GONE
    }

    private fun changeCodePage() {
        signinup_subtitle_textView.text = getString(R.string.input_auth_code)
        signinup_email_inputLayout.visibility = View.GONE
        signinup_authCode_pinView.visibility = View.VISIBLE
        signinup_pinError_textView.text = getString(R.string.auth_code_not_correct)
        signinup_doSign_button.apply {
            text = getString(R.string.confirm)
            signinup_doSign_button.setOnClickListener(null)
            signinup_doSign_button.setBackgroundResource(R.drawable.button_bright_gray)
        }

        signUpFragment.arguments = Bundle().apply {
            putString("Email", signUpRequest.email)
        }
        signUpRequest.email = ""
    }

    private fun activeButton(button: Button) {
        button.apply {
            setBackgroundResource(R.drawable.button_gradient)
            when {
                signUpRequest.scanEnteredEmail() -> {
                    setOnClickListener {
                        signUpViewModel.getAuthCode(signUpRequest.email)
                    }
                }
                signUpRequest.scanEnteredPinCode(pinCode) -> {
                    setOnClickListener {
                        signUpViewModel.checkAuthCode(pinCode)
                    }
                }
                else -> {
                    setBackgroundResource(R.drawable.button_bright_gray)
                    setOnClickListener(null)
                }
            }
        }
    }

    private fun showErrorMessage(currentProcess: SignProcess) {
        when (currentProcess) {
            SignProcess.GetCode -> {
                signinup_email_inputLayout.error = getString(R.string.email_format_not_correct)
            }
            SignProcess.CheckCode -> {
                signinup_pinError_textView.visibility = View.VISIBLE
            }
            else -> {
            }
        }
    }
}