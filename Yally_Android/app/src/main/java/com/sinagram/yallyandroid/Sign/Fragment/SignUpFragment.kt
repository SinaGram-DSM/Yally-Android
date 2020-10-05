package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Data.SignProcess
import com.sinagram.yallyandroid.Sign.Data.SignUpRequest
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.SignUpViewModel
import kotlinx.android.synthetic.main.layout_signinup.*
import kotlinx.android.synthetic.main.signinup_layout.*
import org.w3c.dom.Text

class SignUpFragment : Fragment() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    val email by lazy { requireArguments().getString("Email") }
    var confirm = ""
    var signUpRequest = SignUpRequest("", "", "", 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            signUpRequest.email = email!!
        } catch (e: Exception) {
            Log.e("SignUpFragment", e.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_signinup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            signUp_nickname_editText.addTextChangedListener(
                createTextWatcher(signUp_nickname_inputLayout)
            )
            signUp_password_editText.addTextChangedListener(
                createTextWatcher(signUp_password_inputLayout)
            )
            signUp_confirm_password_editText.addTextChangedListener(
                createTextWatcher(signUp_nickname_inputLayout)
            )
            signUp_age_editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    var text = p0.toString().toInt()
                    if (text > 120) {
                        text /= 10
                        signUp_age_editText.setText(text)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    signUpRequest.age = p0.toString().toInt()
                }

            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val signActivity = activity as SignActivity

        signUpViewModel.errorSignLiveData.observe(viewLifecycleOwner, {
            showMessage()
        })
        signUpViewModel.signUpSuccessLiveData.observe(viewLifecycleOwner, {
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
                    signUp_nickname_inputLayout -> signUpRequest.nickname = p0.toString()
                    signUp_password_inputLayout -> signUpRequest.password = p0.toString()
                    signUp_confirm_password_inputLayout -> confirm = p0.toString()
                }
                activeButton(signUp_doSign_button)
            }
        }
    }

    private fun activeButton(button: Button) {
        button.apply {
            when {
                signUpRequest.password == confirm
                        && signUpRequest.password.length >= 8
                        && signUpRequest.password.isNotBlank()
                        && signUpRequest.age != 0 -> {
                    setBackgroundResource(R.drawable.button_gradient)
                    button.setOnClickListener {
                        signUpViewModel.checkSignUpInfo(signUpRequest)
                    }
                }
                else -> {
                    setBackgroundResource(R.drawable.button_bright_gray)
                    setOnClickListener(null)
                }
            }
        }
    }

    private fun showMessage() {
        signUp_nickname_inputLayout.error = "이미 존재하는 닉네임입니다"
        signUp_password_inputLayout.error = "비밀번호 형식이 올바르지 않습니다"
        signUp_confirm_password_inputLayout.error = "비밀번호가 일치하지 않습니다"
    }
}