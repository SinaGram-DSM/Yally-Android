package com.sinagram.yallyandroid.Network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YallyConnector {
    private var retrofit: Retrofit
    private var api: YallyAPI
    const val baseURL = "http://13.125.238.84:81"
    const val s3 = "https://yally-sinagram.s3.ap-northeast-2.amazonaws.com/"

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(TokenAuthenticator())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(YallyAPI::class.java)
    }

    fun createAPI() = api
}