package com.sinagram.yallyandroid.Network

import android.util.Log
import com.sinagram.yallyandroid.Util.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response

class TokenAuthenticator : Interceptor {
    val sharedPreferencesManager = SharedPreferencesManager.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val mainResponse: Response = chain.proceed(chain.request())

        if (mainResponse.code == 404) {
            sharedPreferencesManager.isLogin = false
            CoroutineScope(Dispatchers.Main).launch {
                val accessToken = SharedPreferencesManager.getInstance().accessToken
                if (accessToken != null) {
                    getAccessToken(accessToken)
                }
            }
        }
        return mainResponse
    }

    private suspend fun getAccessToken(accessToken: String) {
        val token = withContext(Dispatchers.IO) {
            YallyConnector.createAPI().refreshToken(accessToken)
        }

        if (token.isSuccessful) {
            if (token.code() == 200) {
                sharedPreferencesManager.accessToken = token.body()
            } else {
                Log.e("TokenAuthenticator", "알 수 없는 오류")
            }
        } else {
            Log.e("TokenAuthenticator", token.errorBody().toString())
        }
    }
}