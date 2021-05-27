package com.greenhand.cooperativework.http

import com.greenhand.cooperativework.http.api.CommunityService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object CommunityClient {
    private const val BASE_URL = "http://baobab.kaiyanapp.com/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val communityService: CommunityService = retrofit.create(CommunityService::class.java)
}