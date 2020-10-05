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
    private var pinCode = ""

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
            signinup_email_editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    signinup_email_inputLayout.error = null
                }

                override fun afterTextChanged(p0: Editable?) {
                    email = p0.toString()
                    activeButton(signinup_doSign_button)
                }
            })
            signinup_authCode_pinView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    signinup_pinError_textView.text = null
                }

                override fun afterTextChanged(p0: Editable?) {
                    pinCode = p0.toString()
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
                    signActivity.replaceFragment(SignUpFragment().apply {
                        arguments = Bundle().apply {
                            putString("Email", email)
                        }
                    })
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
        signinup_subtitle_textView.text = "등록한 이메일 주소로 전송된 인증 코드를 입력해 주세요"
        signinup_authCode_pinView.visibility = View.VISIBLE
        signinup_doSign_button.text = getString(R.string.confirm)
        signinup_pinError_textView.text = "인증번호가 올바르지 않습니다"
    }

    private fun activeButton(button: Button) {
        button.apply {
            when {
                email.length <= 30 && emailPattern.matcher(email).matches() -> {
                    setBackgroundResource(R.drawable.button_gradient)
                    signUpViewModel.getAuthCode(email)
                }
                pinCode.length == 6 -> {
                    setBackgroundResource(R.drawable.button_gradient)
                    button.setOnClickListener {
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