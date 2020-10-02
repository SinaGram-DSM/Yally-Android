package com.sinagram.yallyandroid.Sign.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Sign.Data.SignRepository
import kotlinx.coroutines.launch
import com.sinagram.yallyandroid.Network.Result

class LoginViewModel : ViewModel() {
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData("")

    fun checkLoginInfo(email: String, password: String) {
        when {
            email.length > 30 -> {
                errorMessageLiveData.value = ""
            }
            password.length < 8 -> {
                errorMessageLiveData.value = ""
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
            val result = SignRepository().doLogin(body)
            when (result) {
                is Result.Success -> {

                }
                is Result.Error -> {

                }
            }
        }
    }
}