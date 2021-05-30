package com.greenhand.cooperativework.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommunityRecommendImageBean(
    val userIconUrl: String,
    val userName: String,
    val releaseCity: String,
    val description: String,
    val likeCount: Int,
    val collectionCount: Int,
    val replyCount: Int,
    val imageUrlList: List<String>
) : Parcelable