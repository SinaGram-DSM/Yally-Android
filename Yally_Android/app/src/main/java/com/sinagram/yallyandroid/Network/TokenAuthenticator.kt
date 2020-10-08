package com.sinagram.yallyandroid.Network

import android.content.Intent
import android.util.Log
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Util.SharedPreferencesManager
import com.sinagram.yallyandroid.Util.YallyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response

class TokenAuthenticator : Interceptor {
    private val sharedPreferencesManager = SharedPreferencesManager.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val mainResponse: Response = chain.proceed(chain.request())

        when (mainResponse.code) {
            401, 422 -> {
                sharedPreferencesManager.apply {
                    isLogin = false
                    accessToken = ""
                }
                val intent = Intent(YallyApplication.context, SignActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                YallyApplication.context!!.startActivity(intent)
            }
            403 -> {
                sharedPreferencesManager.isLogin = false
                CoroutineScope(Dispatchers.Main).launch {
                    val accessToken = SharedPreferencesManager.getInstance().accessToken
                    if (accessToken != null) {
                        getAccessToken(accessToken)
                    }
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