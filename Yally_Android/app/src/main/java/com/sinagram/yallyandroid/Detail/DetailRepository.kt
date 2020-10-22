package com.sinagram.yallyandroid.Detail

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector

class DetailRepository: BaseRepository() {
    suspend fun getCommentList(id: String): Result<CommentResponse> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().getComments(token, id) }
    }
}