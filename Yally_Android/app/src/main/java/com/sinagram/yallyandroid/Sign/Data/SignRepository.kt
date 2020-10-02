package com.sinagram.yallyandroid.Sign.Data

import com.sinagram.yallyandroid.Network.YallyConnector
import com.sinagram.yallyandroid.Network.safeApiCall
import com.sinagram.yallyandroid.Network.Result
import retrofit2.Response

class SignRepository {
    suspend fun doLogin(body: HashMap<String, String>): Result<Any> {
        return getUserRequest(result = { YallyConnector.createAPI().doLogin(body) })
    }

    suspend fun sendAuthCode(body: HashMap<String, String>): Result<Any> {
        return getUserRequest(result = { YallyConnector.createAPI().sendAuthCode(body) })
    }

    suspend fun confirmAuthCode(body: HashMap<String, String>): Result<Any> {
        return getUserRequest(result = { YallyConnector.createAPI().confirmAuthCode(body) })
    }

    suspend fun doSignUp(body: HashMap<String, String>): Result<Any> {
        return getUserRequest(result = { YallyConnector.createAPI().doSignUp(body) })
    }

    private suspend fun <T> getUserRequest(result: suspend () -> Response<T>): Result<Any> {
        return safeApiCall(call = result)
    }
}