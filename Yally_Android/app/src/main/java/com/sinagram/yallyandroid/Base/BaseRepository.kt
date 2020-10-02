package com.sinagram.yallyandroid.Base

import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.safeApiCall
import retrofit2.Response

open class BaseRepository {
    suspend fun <T> MappingToResult(result: suspend () -> Response<T>): Result<Any> {
        return safeApiCall(call = result)
    }
}