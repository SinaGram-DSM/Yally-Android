package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Data.SignProcess
import com.sinagram.yallyandroid.Sign.Data.SignUpRequest
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.SignUpViewModel
import kotlinx.android.synthetic.main.signinup_layout.view.*

class SignUpFragment : Fragment() {
    private val signUpViewModel: SignUpViewModel by viewModels()
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
            signinup_title_textView.text = getString(R.string.welcome_first_visit)
            signinup_subtitle_textView.text = getString(R.string.start_sign_up)
            signinup_doSign_button.text = getString(R.string.sign_in)
            signinup_forgot_textView.visibility = View.GONE
            signinup_doSign_button.setOnClickListener {
                email = signinup_email_editText.text.toString()
                val password = signinup_password_editText.text.toString()

                val signUpRequest = SignUpRequest(email, password, "nickname", 0)

                signUpViewModel.checkSignUpInfo(signUpRequest)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val signActivity = activity as SignActivity

        signActivity.findViewById<TextView>(R.id.sign_title_textView).text =
            getString(R.string.sign_in)

        signUpViewModel.errorMessageLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        signUpViewModel.signUpSuccessLiveData.observe(viewLifecycleOwner, {
            when(it) {
                SignProcess.Create -> {
                    moveToAuthentication()
                }
                else -> {
                    Log.e("SignUpFragment", "알 수 없는 오류")
                }
            }
        })
    }

    private fun moveToAuthentication() {
        (activity as SignActivity).replaceFragment(AuthenticationFragment().apply {
            arguments = Bundle().apply {
                putString("UserEmail", email)
            }
        })
    }
}