package com.sinagram.yallyandroid.Sign.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Data.SignProcess
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Sign.ViewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_authentication.view.*

class AuthenticationFragment : Fragment() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val hashMap: HashMap<String, String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = arguments?.getString("UserEmail")

        if (email != null) {
            hashMap["email"] = email
            signUpViewModel.getAuthCode(hashMap)
        } else {
            Log.e("AuthenticationFragment", "arguments is Null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.authentication_doSign_button.setOnClickListener {
            hashMap.clear()
            hashMap["code"] = view.authentication_authCode_pinView.text.toString()
            signUpViewModel.checkAuthCode(hashMap)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val signActivity = activity as SignActivity

        signUpViewModel.errorMessageLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        signUpViewModel.signUpSuccessLiveData.observe(viewLifecycleOwner, {
            when(it) {
                SignProcess.GetCode -> {
                    Toast.makeText(context, "인증 메일을 보내드렸습니다.", Toast.LENGTH_LONG).show()
                }
                SignProcess.CheckCode -> {
                    signActivity.moveToMain()
                    signActivity.finish()
                }
                else -> {
                    Log.e("AuthenticationFragment", "알 수 없는 오류")
                }
            }
        })
    }
}