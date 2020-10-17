package com.sinagram.yallyandroid.Sign.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Sign.Data.PasswordProcess
import com.sinagram.yallyandroid.Sign.Data.SignRepository
import com.sinagram.yallyandroid.Sign.Data.TokenResponse
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = SignRepository()
    val errorSignLiveData: MutableLiveData<PasswordProcess> = MutableLiveData()
    val loginSuccessLiveData: MutableLiveData<PasswordProcess> = MutableLiveData()

    fun mappingLoginInfo(email: String, password: String) {
        val hashMap = HashMap<String, String>()
        hashMap["email"] = email
        hashMap["password"] = password
        sendLoginInfo(hashMap)
    }

    private fun sendLoginInfo(body: HashMap<String, String>) {
        viewModelScope.launch {
            when (val result = repository.doLogin(body)) {
                is Result.Success -> {
                    loginSuccess(result)
                }
                is Result.Error -> {
                    errorSignLiveData.postValue(PasswordProcess.Login)
                    Log.e("LoginViewModel", result.exception)
                }
            }
        }
    }

    private fun loginSuccess(result: Result.Success<TokenResponse>) {
        if (result.code == 200) {
            loginSuccessLiveData.postValue(PasswordProcess.Login)
            repository.putToken(result.data!!)
            repository.putLoginInfo(true)
        } else {
            errorSignLiveData.postValue(PasswordProcess.Login)
        }
    }

    fun sendResetCode(email: String) {
        val body: HashMap<String, String> = HashMap()
        body["email"] = email

        viewModelScope.launch {
            when (val result = repository.sendResetCode(body)) {
                is Result.Success -> {
                    loginSuccessLiveData.postValue(PasswordProcess.Email)
                }
                is Result.Error -> {
                    errorSignLiveData.postValue(PasswordProcess.Email)
                    Log.e("LoginViewModel", result.exception)
                }
            }
        }
    }

    fun checkResetCode() {
        loginSuccessLiveData.postValue(PasswordProcess.Code)
    }

    fun sendResetPassword(body: HashMap<String, String>) {
        viewModelScope.launch {
            when (val result = repository.changePassword(body)) {
                is Result.Success -> {
                    loginSuccessLiveData.postValue(PasswordProcess.Password)
                    repository.putLoginInfo(true)
                }
                is Result.Error -> {
                    errorSignLiveData.postValue(PasswordProcess.Password)
                    Log.e("LoginViewModel", result.exception)
                }
            }
        }
    }
}