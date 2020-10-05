package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_authentication.view.*
import kotlinx.android.synthetic.main.signinup_layout.*
import kotlinx.android.synthetic.main.signinup_layout.view.*

class AuthenticationFragment : Fragment() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val hashMap: HashMap<String, String> = HashMap()

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
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val signActivity = activity as SignActivity

        signActivity.findViewById<TextView>(R.id.sign_title_textView).text =
            getString(R.string.sign_up)
    }

    private fun changeEmailPage() {
        signinup_title_textView.text = getString(R.string.welcome_first_visit)
        signinup_subtitle_textView.text = getString(R.string.start_sign_up)
        signinup_password_inputLayout.visibility = View.GONE
        signinup_doSign_button.text = getString(R.string.next)
        signinup_forgot_textView.visibility = View.GONE
    }
}