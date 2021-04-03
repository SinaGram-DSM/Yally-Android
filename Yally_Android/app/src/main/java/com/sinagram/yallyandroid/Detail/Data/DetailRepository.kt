package com.sinagram.yallyandroid.Detail.Data

import com.sinagram.yallyandroid.Base.BasePostRepository
import com.sinagram.yallyandroid.Home.Data.EditPostRequest
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

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

    suspend fun sendComment(
        id: String,
        requestHashMap: HashMap<String, RequestBody>
    ): Result<Void> {
        return checkHaveToken { YallyConnector.createAPI().postComment(token, id, requestHashMap) }
    }

    suspend fun editPost(id: String, editPostRequest: EditPostRequest): Result<Void> {
        return checkHaveToken {
            YallyConnector.createAPI()
                .updatePost(
                    token,
                    id,
                    editPostRequest.imgPart!!,
                    editPostRequest.soundPart!!,
                    editPostRequest.requestHashMap
                )
        }
    }
}