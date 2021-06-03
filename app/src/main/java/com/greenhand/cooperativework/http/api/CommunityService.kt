package com.greenhand.cooperativework.http.api

import com.greenhand.cooperativework.bean.CommunityFirstRecommendBean
import com.greenhand.cooperativework.bean.CommunityFollowBean
import com.greenhand.cooperativework.bean.CommunityRecommendBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface CommunityService {

    @GET("v7/community/tab/rec{nextPageUrl}")
    fun getCommunityFirstRecommendList(@Path("nextPageUrl")nextPageUrl:String): Call<CommunityFirstRecommendBean>
    @GET("v7/community/tab/rec")
    fun getCommunityRecommendList(@Query("startScore") startScore:String,@Query("pageCount")pageCount:String): Call<CommunityRecommendBean>
    @GET("v6/community/tab/follow")
    fun getCommunityFollowList(@Query("start") start:String,@Query("num")num:String,@Query("newest")newest:Boolean): Call<CommunityFollowBean>
}