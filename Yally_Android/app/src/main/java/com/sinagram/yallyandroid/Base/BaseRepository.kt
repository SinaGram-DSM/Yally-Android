package com.sinagram.yallyandroid.Base

import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.safeApiCall
import com.sinagram.yallyandroid.Util.SharedPreferencesManager
import retrofit2.Response

open class BaseRepository {
    val sharedPreferences = SharedPreferencesManager.getInstance()

    suspend fun <T : Any> checkHaveToken(result: suspend () -> Response<T>): Result<T> {
        val token = getAccessToken()

        return if (token != null && token.isNotEmpty()) {
            mappingToResult(result)
        } else {
            Result.Error("Token is Null")
        }
    }

    suspend fun <T : Any> mappingToResult(result: suspend () -> Response<T>): Result<T> {
        return safeApiCall(call = result)
    }

    fun getAccessToken(): String? {
        return sharedPreferences.accessToken
    }

    fun getEmail(): String {
        return sharedPreferences.email
    }
}