package com.sinagram.yallyandroid.Detail.Data

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class CommentRequest(
    var content: String = "",
    var file: File? = null,
    var requestHashMap: HashMap<String, RequestBody> = HashMap()
) {
    fun addComment() {
        requestHashMap["content"] = content.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun addFile() {
        file?.let {
            requestHashMap["file"] = file!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
    }
}