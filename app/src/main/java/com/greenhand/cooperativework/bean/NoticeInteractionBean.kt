package com.greenhand.cooperativework.bean

data class NoticeInteractionBean(
    val itemList:ArrayList<Item>,
    val count:Int,
    val total:Int,
    val nextPageUrl:String,
    val adExist:Boolean
) {
    data class Item(
        val type:String,
        val `data`: Data,
        val id:Int,
        val adIndex:Int
    ) {
        data class Data(
            val dataType:String,
            val id:Int,
            val title:String,
            val joinCount:Int,
            val viewCount:Int,
            val showHotSign:Boolean,
            val actionUrl:String,
            val imageUrl:String,
            val haveReward:Boolean,
            val ifNewest:Boolean,
            val newestEndTime:Int
        )
    }
}