package com.sinagram.yallyandroid.Detail.Data

import com.sinagram.yallyandroid.Base.BasePostRepository
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector
import okhttp3.RequestBody

class DetailRepository : BasePostRepository() {
    suspend fun getDetailPost(id: String): Result<Post> {
        return checkHaveToken { YallyConnector.createAPI().getDetailPost(token, id) }
    }

    suspend fun getCommentList(id: String): Result<CommentResponse> {
        return checkHaveToken { YallyConnector.createAPI().getComments(token, id) }
    }

    suspend fun deleteComment(id: String): Result<Void> {
        return checkHaveToken { YallyConnector.createAPI().deleteComment(token, id) }
    }

    suspend fun sendComment(id: String, requestHashMap: HashMap<String, RequestBody>): Result<Void> {
        return checkHaveToken { YallyConnector.createAPI().postComment(token, id, requestHashMap) }
    }
}