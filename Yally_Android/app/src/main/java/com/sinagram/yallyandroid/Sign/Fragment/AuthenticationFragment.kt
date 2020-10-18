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
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.SignUpViewModel
import com.sinagram.yallyandroid.Util.TextWatcherImpl
import kotlinx.android.synthetic.main.signinup_layout.*
import kotlinx.android.synthetic.main.signinup_layout.view.*
import java.util.regex.Pattern

class AuthenticationFragment : Fragment() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val emailPattern =
        Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+")
    private var email = ""
    private var pinCode = ""
    private var signUpFragment = SignUpFragment()

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
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    signinup_email_inputLayout.error = null
                }
                override fun afterTextChanged(s: Editable?) {
                    email = s.toString()
                    activeButton(signinup_doSign_button)
                }
            })
            signinup_authCode_pinView.addTextChangedListener(object : TextWatcherImpl() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    signinup_pinError_textView.text = null
                }
                override fun afterTextChanged(s: Editable?) {
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
                    signUpFragment.arguments = Bundle().apply {
                        putString("Email", email)
                    }
                    email = ""
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
            putString("Email", email)
        }
        email = ""
    }

    private fun activeButton(button: Button) {
        button.apply {
            when {
                email.length <= 30 && emailPattern.matcher(email).matches() -> {
                    setBackgroundResource(R.drawable.button_gradient)
                    setOnClickListener {
                        signUpViewModel.getAuthCode(email)
                    }
                }
                pinCode.length == 6 -> {
                    setBackgroundResource(R.drawable.button_gradient)
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