package com.sinagram.yallyandroid.Home.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector
import com.sinagram.yallyandroid.Network.safeApiCall
import retrofit2.Response

class HomeRepository: BaseRepository() {
    suspend fun getMainTimeLine(pageNum: Int): Result<PostsResponse> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().getMainTimeLine(token, pageNum) }
    }

    suspend fun doYally(id: String): Result<Void> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().doYallyOnPost(token, id) }
    }

    suspend fun cancelYally(id: String): Result<Void> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().cancelYallyOnPost(token, id) }
    }

    suspend fun getListeningList(): Result<ListeningResponse> {
        val token = getAccessToken()!!
        val email = getEmail()
        return checkHaveToken { YallyConnector.createAPI().getListeningList(token, email) }
    }

    suspend fun doListening(email: String): Result<Void> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().doListening(token, email) }
    }

    suspend fun cancelListening(email: String): Result<Void> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().cancelListening(token, email) }
    }

    private fun getAccessToken(): String? {
        return sharedPreferences.accessToken
    }

    private fun getEmail(): String {
        return sharedPreferences.email
    }

    private suspend fun <T : Any> checkHaveToken(result: suspend () -> Response<T>): Result<T> {
        val token = getAccessToken()

        return if (token != null && token.isNotEmpty()) {
            mappingToResult(result)
        } else {
            Result.Error("Token is Null")
        }
    }
}