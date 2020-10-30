package com.sinagram.yallyandroid.Home.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector

class SearchRepository : BaseRepository() {
    private val token = getAccessToken()!!

    suspend fun getFriendList(): Result<FriendResponse> {
        return checkHaveToken { YallyConnector.createAPI().getListOfRecommendedFriends(token) }
    }

    suspend fun getListeningList(): Result<ListeningResponse> {
        return checkHaveToken {
            YallyConnector.createAPI().getListeningList(token.substring(7), getEmail())
        }
    }

    suspend fun getPostsBySearchHashtag(tag: String, page: Int): Result<SearchPostsResponse> {
        return checkHaveToken { YallyConnector.createAPI().searchHashtag(token, tag, page) }
    }

    suspend fun getUserListBySearchName(name: String): Result<UserResponse> {
        return checkHaveToken { YallyConnector.createAPI().searchUser(token, name) }
    }
}