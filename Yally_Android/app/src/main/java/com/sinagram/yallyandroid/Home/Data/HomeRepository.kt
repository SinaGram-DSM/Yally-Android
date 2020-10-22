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

    suspend fun deletePost(id: String): Result<Void> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().deletePost(token, id) }
    }

    suspend fun doListening(email: String): Result<Void> {
        val token = getAccessToken()!!
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["email"] = email

        return checkHaveToken { YallyConnector.createAPI().doListening(token, hashMap) }
    }

    suspend fun cancelListening(email: String): Result<Void> {
        val token = getAccessToken()!!
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["email"] = email

        return checkHaveToken { YallyConnector.createAPI().cancelListening(token, hashMap) }
    }

    suspend fun getListeningList(): Result<ListeningResponse> {
        val token = getAccessToken()!!
        val email = getEmail()
        return checkHaveToken { YallyConnector.createAPI().getListeningList(token, email) }
    }

    private fun getEmail(): String {
        return sharedPreferences.email
    }
}