package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.LoginViewModel

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signinup_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().findViewById<TextView>(R.id.sign_title_textView).text =
            getString(R.string.login)
    }
}