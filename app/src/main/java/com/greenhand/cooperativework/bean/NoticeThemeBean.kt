package com.greenhand.cooperativework.bean

data class NoticeThemeBean(val tabInfo:TabInfo){
    data class TabInfo(
        val tabList:List<Tab>,
        val defaultIndex:Int
        ){
        data class Tab(
            val id:Int,
            val name:String,
            val apiUrl:String,
            val tabType:Int,
            val nameType:Int
            )
    }
}