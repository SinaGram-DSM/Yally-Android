package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Data.PasswordProcess
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.LoginViewModel
import kotlinx.android.synthetic.main.signinup_layout.*
import kotlinx.android.synthetic.main.signinup_layout.signinup_email_inputLayout
import kotlinx.android.synthetic.main.signinup_layout.signinup_password_inputLayout
import kotlinx.android.synthetic.main.signinup_layout.view.*
import java.util.regex.Pattern

class ResetPasswordFragment : Fragment() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val hashMap: HashMap<String, String> = HashMap()
    private val emailPattern =
        Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+")
    private var email = ""
    private var pinCode = ""
    private var password = "pass"
    private var confirm = "word"

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
            signinup_authCode_pinView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    pinCode = p0.toString()
                    signinup_pinError_textView.visibility = View.GONE
                    activeButton(signinup_doSign_button)
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val signActivity = activity as SignActivity

        loginViewModel.errorMessageLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        loginViewModel.errorSignLiveData.observe(viewLifecycleOwner, {
            showErrorMessage(it)
        })

        loginViewModel.loginSuccessLiveData.observe(viewLifecycleOwner, {
            when (it) {
                PasswordProcess.Email -> {
                    hashMap["email"] = email
                    email = ""
                    changeCodePage()
                }
                PasswordProcess.Code -> {
                    hashMap["code"] = pinCode
                    pinCode = ""
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
        signinup_subtitle_textView.text = "가입 시 등록한 이메일을 입력해 주세요"
        signinup_doSign_button.text = getString(R.string.next)
        signinup_forgot_textView.visibility = View.GONE
    }

    private fun changeCodePage() {
        signinup_email_inputLayout.visibility = View.GONE
        signinup_authCode_pinView.visibility = View.VISIBLE
        signinup_subtitle_textView.text = "메일로 발송된 비밀번호 재설정 코드를 입력해 주세요"
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
        signinup_subtitle_textView.text = "새로운 비밀번호를 입력해 주세요"
        signinup_doSign_button.apply {
            text = getString(R.string.initialization)
            setBackgroundResource(R.drawable.button_bright_gray)
            setOnClickListener(null)
        }
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
                    signinup_password_inputLayout -> password = p0.toString()
                    signinup_comfirm_password_inputLayout -> confirm = p0.toString()
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
                    button.setOnClickListener {
                        loginViewModel.sendResetCode(email)
                    }
                }
                pinCode.length == 6 -> {
                    setBackgroundResource(R.drawable.button_gradient)
                    button.setOnClickListener {
                        loginViewModel.checkResetCode(pinCode)
                    }
                }
                password == confirm && password.length >= 8 && password.isNotBlank() -> {
                    setBackgroundResource(R.drawable.button_gradient)
                    button.setOnClickListener {
                        hashMap["password"] = password
                        loginViewModel.sendResetPassword(hashMap)
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
                signinup_email_inputLayout.error = "존재하지 않는 계정입니다"
            }
            PasswordProcess.Code -> {
                signinup_pinError_textView.visibility = View.VISIBLE
            }
            PasswordProcess.Password -> {
                signinup_password_inputLayout.error = "비밀번호 형식이 올바르지 않습니다"
                signinup_comfirm_password_inputLayout.error = "비밀번호가 일치하지 않습니다"
            }
            else -> {
            }
        }
    }
}