package com.greenhand.cooperativework.bean

class NoticeMessageBean (
    var title:String,
    var content:String,
    var date: Int,
    var icon:String,
    var viewed:Boolean,
    var actionURL:String
    ){

        var dateString = dateToString()

        var viewStateString = viewStateToString()

        private fun dateToString(): String {
            //TODO
            return ""
        }

        private fun viewStateToString():String{
            return if(viewed)
                "已读"
            else
                "未读"
        }
}