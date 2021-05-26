package com.greenhand.cooperativework.http

import com.greenhand.cooperativework.http.api.WelcomeImgService
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
object NetWork {
    private val retrofitImage = Retrofit.Builder()
        .baseUrl("https://cn.bing.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val welcomeImgService = retrofitImage.create(WelcomeImgService::class.java)
    suspend fun getWelcomeImgUrlResponse() = welcomeImgService.getImageData().await()
}