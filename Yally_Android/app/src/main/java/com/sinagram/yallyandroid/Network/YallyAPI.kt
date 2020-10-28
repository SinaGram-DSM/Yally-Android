package com.sinagram.yallyandroid.Network

import com.sinagram.yallyandroid.Detail.Data.CommentResponse
import com.sinagram.yallyandroid.Home.Data.ListeningResponse
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostsResponse
import com.sinagram.yallyandroid.Sign.Data.SignUpRequest
import com.sinagram.yallyandroid.Sign.Data.TokenResponse
import okhttp3.RequestBody
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

    @POST("/user/listening")
    suspend fun doListening(
        @Header("Authorization") header: String,
        @Body body: HashMap<String, String>
    ): Response<Void>

    @DELETE("/user/listening")
    suspend fun cancelListening(
        @Header("Authorization") header: String,
        @Body body: HashMap<String, String>
    ): Response<Void>


    @GET("/timeline/{page}")
    suspend fun getMainTimeLine(
        @Header("Authorization") header: String,
        @Path("page") page: Int
    ): Response<PostsResponse>


    @GET("/post/{id}")
    suspend fun getDetailPost(
        @Header("Authorization") header: String,
        @Path("id") id: String
    ): Response<Post>

    @GET("/post/{id}/comment")
    suspend fun getComments(
        @Header("Authorization") header: String,
        @Path("id") id: String
    ): Response<CommentResponse>

    @DELETE("/post/{id}")
    suspend fun deletePost(
        @Header("Authorization") header: String,
        @Path("id") id: String
    ): Response<Void>

    @Multipart
    @POST("/post/comment/{id}")
    suspend fun postComment(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @PartMap partMap: HashMap<String, RequestBody>
    ): Response<Void>

    @DELETE("/post/comment/{commentId}")
    suspend fun deleteComment(
        @Header("Authorization") header: String,
        @Path("commentId") id: String
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