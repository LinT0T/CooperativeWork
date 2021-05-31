package com.greenhand.cooperativework.bean

import android.content.Context
import android.util.Log
import android.view.View
import com.greenhand.cooperativework.utils.TimeUtil
import com.greenhand.cooperativework.utils.toast
import java.util.*

class NoticeMessageBean (

    var title:String,
    var content:String,
    var date: Long,
    var icon:String,
    var viewed:Boolean,
    var actionURL:String
    ){

        var dateString = dateToString()

        var viewStateString = viewStateToString()



    private fun dateToString(): String {
            return TimeUtil.getTime(Date(date))
        }

        private fun viewStateToString():String{
            return if(viewed)
                "已读"
            else
                "未读"
        }

    inner class EventHandle(){
        fun onItemSingleClick(view: View){
            "即将为您打开网页。".toast()
        }
    }

}