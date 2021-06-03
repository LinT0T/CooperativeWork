package com.greenhand.cooperativework.repository.fragment

import com.greenhand.cooperativework.bean.CommunityFollowBean
import com.greenhand.cooperativework.http.CommunityClient
import retrofit2.Call

class CommunityFollowRepository {
    fun getCommunityFollow(start: String, num: String, newest: Boolean): Call<CommunityFollowBean> =
        CommunityClient.communityService.getCommunityFollowList(start, num, newest)
}