package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.ViewModel.LoginViewModel
import kotlinx.android.synthetic.main.signinup_layout.view.*

class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by viewModels()

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
            signinup_doSign_button.setOnClickListener {
                val email = signinup_email_editText.text.toString()
                val password = signinup_password_editText.text.toString()
                loginViewModel.checkLoginInfo(email, password)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().findViewById<TextView>(R.id.sign_title_textView).text =
            getString(R.string.login)
    }
}