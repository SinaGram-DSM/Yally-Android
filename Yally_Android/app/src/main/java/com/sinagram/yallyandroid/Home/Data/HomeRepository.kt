package com.sinagram.yallyandroid.Home.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector

class HomeRepository: BaseRepository() {
    suspend fun getMainTimeLine(pageNum: Int): Result<PostsResponse> {
        val token = getAccessToken()

        return if (token != null && token.isNotEmpty()) {
            mappingToResult { YallyConnector.createAPI().getMainTimeLine(token, pageNum) }
        } else {
            Result.Error("Token is Null")
        }
    }

    private fun getAccessToken(): String? {
        return sharedPreferences.accessToken
    }
}