package com.sinagram.yallyandroid.Sign.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Base.BaseViewModel
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Sign.Data.SignProcess
import com.sinagram.yallyandroid.Sign.Data.SignRepository
import com.sinagram.yallyandroid.Sign.Data.SignUpRequest
import kotlinx.coroutines.launch

class SignUpViewModel: BaseViewModel() {
    private val repository = SignRepository()
    val errorSignLiveData: MutableLiveData<SignProcess> = MutableLiveData()
    val signUpSuccessLiveData: MutableLiveData<SignProcess> = MutableLiveData()

    fun getAuthCode(email: String) {
        val body: HashMap<String, String> = HashMap()
        body["email"] = email

        viewModelScope.launch {
            when (val result = repository.sendAuthCode(body)) {
                is Result.Success -> {
                    getAuthCodeSuccess(result)
                }
                is Result.Error -> {
                    Log.e("SignUpViewModel", result.exception)
                }
            }
        }
    }

    private fun getAuthCodeSuccess(result: Result.Success<Void>) {
        if (result.code == 200) {
            signUpSuccessLiveData.postValue(SignProcess.GetCode)
        } else {
            errorSignLiveData.postValue(SignProcess.GetCode)
        }
    }

    fun checkAuthCode(code: String) {
        val body: HashMap<String, String> = HashMap()
        body["code"] = code

        viewModelScope.launch {
            when (val result = repository.confirmAuthCode(body)) {
                is Result.Success -> {
                    checkAuthCodeSuccess(result)
                }
                is Result.Error -> {
                    Log.e("SignUpViewModel", result.exception)
                }
            }
        }
    }

    private fun checkAuthCodeSuccess(result: Result.Success<Void>) {
        if (result.code == 200) {
            signUpSuccessLiveData.postValue(SignProcess.CheckCode)
        } else {
            errorSignLiveData.postValue(SignProcess.CheckCode)
        }
    }

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
                    createUserSuccess(result)
                }
                is Result.Error -> {
                    Log.e("SignUpViewModel", result.exception)
                }
            }
        }
    }

    private fun createUserSuccess(result: Result.Success<Void>) {
        if (result.code == 201) {
            signUpSuccessLiveData.postValue(SignProcess.Create)
        } else {
            errorMessageLiveData.postValue("현재 가입된 사용자 정보와 중복됩니다.")
        }
    }
}