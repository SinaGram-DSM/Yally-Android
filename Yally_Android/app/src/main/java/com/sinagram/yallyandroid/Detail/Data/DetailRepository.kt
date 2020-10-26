package com.sinagram.yallyandroid.Detail.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DetailRepository : BaseRepository() {
    suspend fun getDetailPost(id: String): Result<Post> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().getDetailPost(token, id) }
    }

    suspend fun getCommentList(id: String): Result<CommentResponse> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().getComments(token, id) }
    }

    suspend fun deleteComment(id: String): Result<Void> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().deleteComment(token, id) }
    }

    suspend fun sendComment(
        id: String, requestHashMap: HashMap<String, RequestBody>
    ): Result<Void> {

        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().postComment(token, id, requestHashMap) }
    }

    suspend fun doYally(id: String): Result<Void> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().doYallyOnPost(token, id) }
    }

    suspend fun cancelYally(id: String): Result<Void> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().cancelYallyOnPost(token, id) }
    }
}