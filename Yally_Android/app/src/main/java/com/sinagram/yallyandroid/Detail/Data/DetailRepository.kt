package com.sinagram.yallyandroid.Detail.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DetailRepository : BaseRepository() {
    suspend fun getCommentList(id: String): Result<CommentResponse> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().getComments(token, id) }
    }

    suspend fun deleteComment(id: String): Result<Void> {
        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().deleteComment(token, id) }
    }

    suspend fun sendComment(content: String, file: File?): Result<Void> {
        val mapRequestBody = HashMap<String, RequestBody>()
        mapRequestBody["content"] = content.toRequestBody("text/plain".toMediaTypeOrNull())
        file?.let {
            mapRequestBody["file"] = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        }

        val token = getAccessToken()!!
        return checkHaveToken { YallyConnector.createAPI().postComment(token, mapRequestBody) }
    }
}