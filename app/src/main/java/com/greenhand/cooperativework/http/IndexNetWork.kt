package com.greenhand.cooperativework.http

import com.greenhand.cooperativework.http.api.IndexService
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/1
 */
object IndexNetWork {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://baobab.kaiyanapp.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val indexDiscoverService = retrofit.create(IndexService::class.java)
    suspend fun getIndexDiscoverBean() = indexDiscoverService.getDiscoverBean().await()
}