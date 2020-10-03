package com.sinagram.yallyandroid.Network

import com.sinagram.yallyandroid.Sign.Data.SignUpRequest
import com.sinagram.yallyandroid.Sign.Data.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface YallyAPI {
    @POST("/user/auth")
    suspend fun doLogin(@Body body: HashMap<String, String>): Response<TokenResponse>

    @POST("/user/auth-code/email")
    suspend fun sendAuthCode(@Body body: HashMap<String, String>): Response<Void>

    @POST("/user/code")
    suspend fun confirmAuthCode(@Body body: HashMap<String, String>): Response<Void>

    @POST("/user")
    suspend fun doSignUp(@Body body: SignUpRequest): Response<Void>

    @POST("/user/auth/refresh")
    suspend fun refreshToken(@Header("Authorization") header: String): Response<String>
}