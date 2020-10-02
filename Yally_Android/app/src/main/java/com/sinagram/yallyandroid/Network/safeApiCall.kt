package com.sinagram.yallyandroid.Network

import retrofit2.Response

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Result<Any> {
    return try {
        val myResp = call.invoke()

        if (myResp.isSuccessful) {
            Result.Success(myResp.body()!!, myResp.code())
        } else {
            Result.Error(myResp.errorBody().toString())
        }

    } catch (e: Exception) {
        Result.Error(e.message ?: "Internet error runs")
    }
}