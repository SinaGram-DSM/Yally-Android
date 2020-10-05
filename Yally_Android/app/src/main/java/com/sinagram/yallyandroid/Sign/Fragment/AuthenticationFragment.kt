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
import com.sinagram.yallyandroid.Sign.Data.PasswordProcess
import com.sinagram.yallyandroid.Sign.Data.SignProcess
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.SignUpViewModel
import kotlinx.android.synthetic.main.signinup_layout.*
import kotlinx.android.synthetic.main.signinup_layout.view.*
import java.util.regex.Pattern

class AuthenticationFragment : Fragment() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val emailPattern =
        Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+")
    private var email = ""

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
                    signActivity.replaceFragment(SignUpFragment())
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

    }

    private fun createTextWatcher(textInputLayout: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textInputLayout.error = null
            }

            override fun afterTextChanged(p0: Editable?) {
                when (textInputLayout) {
                    signinup_email_inputLayout -> email = p0.toString()
                }
                activeButton(signinup_doSign_button)
            }
        }
    }

    private fun activeButton(button: Button) {
        button.apply {
            when {
                email.length <= 30 && emailPattern.matcher(email).matches() -> {
                    setBackgroundResource(R.drawable.button_gradient)
                    signUpViewModel.getAuthCode(email)
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
                signinup_email_inputLayout.error = "존재하지 않는 계정입니다"
            }
            SignProcess.CheckCode -> {
                signinup_pinError_textView.visibility = View.VISIBLE
            }
            else -> {
            }
        }
    }
}