package com.sinagram.yallyandroid.Network

import com.sinagram.yallyandroid.Writing.Data.WritingRequest
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface YallyAPI {
    @Multipart
    @POST("/post")
    suspend fun writing (
        @Part part: HashMap<String,RequestBody>
    ): Response<Void>
}