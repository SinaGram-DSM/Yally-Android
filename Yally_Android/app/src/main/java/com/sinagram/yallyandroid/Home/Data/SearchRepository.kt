package com.sinagram.yallyandroid.Home.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector

class SearchRepository: BaseRepository() {
    suspend fun getFriendList(): Result<FriendResponse> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().getListOfRecommendedFriends(token) }
    }

    suspend fun getListeningList(): Result<ListeningResponse> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().getListeningList(token, getEmail()) }
    }
}