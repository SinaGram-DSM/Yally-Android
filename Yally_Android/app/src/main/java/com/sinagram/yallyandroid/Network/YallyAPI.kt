package com.sinagram.yallyandroid.Network

import com.sinagram.yallyandroid.Home.Data.ListeningResponse
import com.sinagram.yallyandroid.Home.Data.PostsResponse
import com.sinagram.yallyandroid.Sign.Data.SignUpRequest
import com.sinagram.yallyandroid.Sign.Data.TokenResponse
import retrofit2.Response
import retrofit2.http.*

interface YallyAPI {
    @POST("/user/auth")
    suspend fun doLogin(@Body body: HashMap<String, String>): Response<TokenResponse>

    @POST("/user/auth-code/email")
    suspend fun sendAuthCode(@Body body: HashMap<String, String>): Response<Void>

    @POST("/user/auth-code")
    suspend fun confirmAuthCode(@Body body: HashMap<String, String>): Response<Void>

    @POST("/user")
    suspend fun doSignUp(@Body body: SignUpRequest): Response<Void>

    @POST("/user/reset-code/email")
    suspend fun sendResetCode(@Body body: HashMap<String, String>): Response<Void>

    @POST("/user/auth/password")
    suspend fun sendResetPassword(@Body body: HashMap<String, String>): Response<Void>

    @POST("/user/auth/refresh")
    suspend fun refreshToken(@Header("Authorization") header: String): Response<String>

    @GET("/timeline/{page}")
    suspend fun getMainTimeLine(
        @Header("Authorization") header: String,
        @Path("page") page: Int
    ): Response<PostsResponse>

    @POST("/user/listening")
    suspend fun doListening(
        @Header("Authorization") header: String,
        listeningEmail: String
    ): Response<Void>

    @DELETE("/user/listening")
    suspend fun cancelListening(
        @Header("Authorization") header: String,
        listeningEmail: String
    ): Response<Void>


    @GET("/post/yally/{id}")
    suspend fun doYallyOnPost(
        @Header("Authorization") header: String,
        @Path("id") id: String
    ): Response<Void>

    @DELETE("/post/yally/{id}")
    suspend fun cancelYallyOnPost(
        @Header("Authorization") header: String,
        @Path("id") id: String
    ): Response<Void>

    @GET("/profile/{email}/listening")
    suspend fun getListeningList(
        @Header("Authorization") header: String,
        @Path("email") email: String
    ): Response<ListeningResponse>
}