package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Data.SignUpRequest
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.SignUpViewModel
import com.sinagram.yallyandroid.Util.TextWatcherImpl
import kotlinx.android.synthetic.main.layout_signinup.*

class SignUpFragment : Fragment() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    var signUpRequest = SignUpRequest()
    var confirm = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            signUpRequest.email = requireArguments().getString("Email")!!
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
                createTextWatcher(signUp_confirm_password_inputLayout)
            )
            signUp_age_editText.addTextChangedListener(object : TextWatcherImpl() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    signUpRequest.age = s.toString().toInt()
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
            signActivity.replaceFragment(LoginFragment())
        })
    }

    private fun createTextWatcher(textInputLayout: TextInputLayout): TextWatcher {
        return object : TextWatcherImpl() {
            override fun afterTextChanged(s: Editable?) {
                textInputLayout.error = null
                when (textInputLayout) {
                    signUp_nickname_inputLayout -> signUpRequest.nickname = s.toString()
                    signUp_password_inputLayout -> signUpRequest.password = s.toString()
                    signUp_confirm_password_inputLayout -> confirm = s.toString()
                }
                activeButton(signUp_doSign_button)
            }
        }
    }

    private fun activeButton(button: Button) {
        button.apply {
            when {
                signUpRequest.scanEnteredSignUpInformation(confirm) -> {
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
        signUp_nickname_inputLayout.error = getString(R.string.nickname_was_exist)
        signUp_password_inputLayout.error = getString(R.string.password_format_not_correct)
        signUp_confirm_password_inputLayout.error = getString(R.string.password_not_equals)
    }
}