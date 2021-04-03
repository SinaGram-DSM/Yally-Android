package com.sinagram.yallyandroid.Home.Data

import com.sinagram.yallyandroid.Base.BasePostRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector

class HomeRepository: BasePostRepository() {
    suspend fun getMainTimeLine(pageNum: Int): Result<PostsResponse> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().getMainTimeLine(token, pageNum) }
    }
}