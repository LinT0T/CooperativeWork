package com.greenhand.cooperativework.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class VideoDetailsBean(
    val videoTitle: String,
    val videoUrl: String,
    val videoId: String,
    val videoDescription: String,
    val likeCount: Int,
    val collectionCount: Int,
    val replyCount: Int,
    val authorIcon: String,
    val authorName: String,
    val authorDescription: String?
) : Parcelable