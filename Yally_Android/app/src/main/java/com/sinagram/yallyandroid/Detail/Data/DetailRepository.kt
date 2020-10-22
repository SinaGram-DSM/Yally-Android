package com.sinagram.yallyandroid.Detail.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector

class DetailRepository: BaseRepository() {
    suspend fun getCommentList(id: String): Result<CommentResponse> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().getComments(token, id) }
    }

    suspend fun deleteComment(id: String): Result<Void> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().deleteComment(token, id) }
    }
}