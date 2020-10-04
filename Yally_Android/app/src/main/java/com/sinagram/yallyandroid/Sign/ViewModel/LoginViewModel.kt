package com.sinagram.yallyandroid.Sign.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Base.BaseViewModel
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Sign.Data.SignRepository
import com.sinagram.yallyandroid.Sign.Data.TokenResponse
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    private val repository = SignRepository()
    val loginSuccessLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun checkLoginInfo(email: String, password: String) {
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
                    errorMessageLiveData.postValue("로그인 실패햐였습니다.\n잠시후 다시 한 번 시도해 주시길 바랍니다.")
                    Log.e("LoginViewModel", result.exception)
                }
            }
        }
    }

    private fun loginSuccess(result: Result.Success<TokenResponse>) {
        if (result.code == 200) {
            loginSuccessLiveData.postValue(true)
            repository.putToken(result.data)
            repository.putLoginInfo(true)
        } else {
            errorMessageLiveData.postValue("존재하지 않는 계정입니다.\n입력한 정보를 다시 한 번 확인해 주시길 바랍니다.")
        }
    }
}