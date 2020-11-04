package com.sinagram.yallyandroid.Home.Data

import com.sinagram.yallyandroid.Base.BasePostRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector

class SearchRepository : BasePostRepository() {
    private val token = getAccessToken()!!

    suspend fun getFriendList(): Result<FriendResponse> {
        return checkHaveToken { YallyConnector.createAPI().getListOfRecommendedFriends(token) }
    }

    suspend fun getPostsBySearchHashtag(tag: String, page: Int): Result<PostsResponse> {
        return checkHaveToken { YallyConnector.createAPI().searchHashtag(token, tag, page) }
    }

    suspend fun getUserListBySearchName(name: String): Result<UserResponse> {
        return checkHaveToken { YallyConnector.createAPI().searchUser(token, name) }
    }
}