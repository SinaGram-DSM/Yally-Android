package com.sinagram.yallyandroid.Network

import retrofit2.http.Multipart
import retrofit2.http.POST

interface YallyAPI{
    @Multipart
    @POST("/post")
    suspend fun writing(

    )
}