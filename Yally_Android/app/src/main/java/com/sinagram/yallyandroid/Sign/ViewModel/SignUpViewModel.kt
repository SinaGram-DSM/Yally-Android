package com.sinagram.yallyandroid.Sign.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Base.BaseViewModel
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Sign.Data.SignRepository
import com.sinagram.yallyandroid.Sign.Data.SignUpRequest
import kotlinx.coroutines.launch

class SignUpViewModel: BaseViewModel() {
    private val repository = SignRepository()
    val signUpSuccessLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkSignUpInfo(signUpRequest: SignUpRequest) {
        when {
            signUpRequest.email.length > 30 -> {
                errorMessageLiveData.value = "이메일을 30자 이하로 작성해 주시길 바랍니다."
            }
            signUpRequest.password.length > 20 -> {
                errorMessageLiveData.value = "비밀번호를 20자 이하로 작성해 주시길 바랍니다."
            }
            else -> {
                createUser(signUpRequest)
            }
        }
    }

    private fun createUser(body: SignUpRequest) {
        viewModelScope.launch {
            when (val result = repository.doSignUp(body)) {
                is Result.Success -> {
                    signUpSuccess(result)
                }
                is Result.Error -> {
                    Log.e("SignUpViewModel", result.exception)
                }
            }
        }
    }

    private fun signUpSuccess(result: Result.Success<Void>) {
        if (result.code == 201) {
            signUpSuccessLiveData.postValue(true)
        } else {
            errorMessageLiveData.postValue("현재 가입된 사용자 정보와 중복됩니다.")
        }
    }
}