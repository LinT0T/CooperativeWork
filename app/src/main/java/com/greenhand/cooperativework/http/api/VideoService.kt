package com.greenhand.cooperativework.http.api

import com.greenhand.cooperativework.bean.RelevantVideoBean
import com.greenhand.cooperativework.bean.VideoReplyBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoService {
    @GET("v4/video/related")
    fun getRelevantVideo(@Query("id") id: String?): Call<RelevantVideoBean>

    @GET("v2/replies/video")
    fun getVideoReply(@Query("videoId") videoId: String?): Call<VideoReplyBean>
}