package com.sinagram.yallyandroid.Sign.ViewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Base.BaseViewModel
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Sign.Data.SignRepository
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    fun checkLoginInfo(email: String, password: String) {
        when {
            email.length > 30 -> {
                errorMessageLiveData.value = "이메일을 30자 이하로 작성해 주시길 바랍니다."
            }
            password.length < 8 -> {
                errorMessageLiveData.value = "비밀번호를 최소 8자 이상 작성해 주시길 바랍니다."
            }
            else -> {
                val hashMap = HashMap<String, String>()
                hashMap["email"] = email
                hashMap["password"] = password
                sendLoginInfo(hashMap)
            }
        }
    }

    private fun sendLoginInfo(body: HashMap<String, String>) {
        viewModelScope.launch {
            when (val result = SignRepository().doLogin(body)) {
                is Result.Success -> {
                    if (result.code != 200) {
                        errorMessageLiveData.postValue("가입되지 않은 정보입니다.")
                    } else {

                    }
                }
                is Result.Error -> {
                    Log.e("LoginViewModel", result.exception)
                }
            }
        }
    }
}