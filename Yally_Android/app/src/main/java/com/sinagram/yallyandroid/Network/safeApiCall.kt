package com.sinagram.yallyandroid.Network

import retrofit2.Response

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    return try {
        val myResp = call.invoke()

        if (myResp.isSuccessful) {
            Result.Success(myResp.body(), myResp.code())
        } else {
            Result.Error(myResp.message())
        }
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }
}