package com.sinagram.yallyandroid.Base

import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.safeApiCall
import com.sinagram.yallyandroid.Util.SharedPreferencesManager
import retrofit2.Response

open class BaseRepository {
    val sharedPreferences = SharedPreferencesManager.getInstance()

    suspend fun <T : Any> mappingToResult(result: suspend () -> Response<T>): Result<T> {
        return safeApiCall(call = result)
    }
}
