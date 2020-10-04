package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
            signinup_email_editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    signinup_email_inputLayout.error = null
                }

                override fun afterTextChanged(p0: Editable?) {
                    email = p0.toString()
                    activeButton(this@apply)
                }
            })
            signinup_password_editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    signinup_password_inputLayout.error = null
                }

                override fun afterTextChanged(p0: Editable?) {
                    password = p0.toString()
                    activeButton(this@apply)
                }
            })
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

    private fun activeButton(view: View) {
        view.signinup_doSign_button.background =
            if (email.length <= 30 && password.length >= 8) {
                signinup_doSign_button.setOnClickListener {
                    loginViewModel.checkLoginInfo(email, password)
                }
                ResourcesCompat.getDrawable(resources, R.drawable.button_gradient, null)
            } else {
                signinup_doSign_button.setOnClickListener {}
                ResourcesCompat.getDrawable(resources, R.drawable.button_bright_gray, null)
            }
    }

    private fun showErrorMessage() {
        signinup_email_inputLayout.error = "존재하지 않는 계정입니다"
        signinup_password_inputLayout.error = "비밀번호가 올바르지 않습니다"
    }
}