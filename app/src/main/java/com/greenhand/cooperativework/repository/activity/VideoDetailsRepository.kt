package com.greenhand.cooperativework.repository.activity

import com.greenhand.cooperativework.bean.RelevantVideoBean
import com.greenhand.cooperativework.bean.VideoReplyBean
import com.greenhand.cooperativework.http.VideoClient
import retrofit2.Call

class VideoDetailsRepository {
    fun getRelevantVideoById(id: String?): Call<RelevantVideoBean> =
        VideoClient.videoService.getRelevantVideo(id)
    fun getVideoReplyById(id: String?): Call<VideoReplyBean> =
       VideoClient.videoService.getVideoReply(id)
}