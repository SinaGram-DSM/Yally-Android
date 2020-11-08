package com.sinagram.yallyandroid.Home.Data

import com.sinagram.yallyandroid.Base.BasePostRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector
import okhttp3.RequestBody

class HomeRepository: BasePostRepository() {
    suspend fun getMainTimeLine(pageNum: Int): Result<PostsResponse> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().getMainTimeLine(token, pageNum) }
    }

    suspend fun editPost(id: String, requestHashMap: HashMap<String, RequestBody>): Result<Void> {
        return checkHaveToken { YallyConnector.createAPI().updatePost(token, id, requestHashMap) }
    }
}