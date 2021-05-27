package com.greenhand.cooperativework.repository.fragment

import com.greenhand.cooperativework.bean.CommunityFirstRecommendBean
import com.greenhand.cooperativework.bean.CommunityRecommendBean
import com.greenhand.cooperativework.http.CommunityClient
import retrofit2.Call

class CommunityRecommendRepository {
    fun getCommunityFirstRecommendByPage(nextPageUrl: String): Call<CommunityFirstRecommendBean> =
        CommunityClient.communityService.getCommunityFirstRecommendList(nextPageUrl)
    fun getCommunityRecommendByPage(startScore: String,pageCount:String): Call<CommunityRecommendBean> =
        CommunityClient.communityService.getCommunityRecommendList(startScore,pageCount)

}