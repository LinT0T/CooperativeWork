package com.greenhand.cooperativework.view.fragment.notice

import android.view.View
import androidx.fragment.app.Fragment
import com.greenhand.cooperativework.bean.NoticeInteractionBean
import com.greenhand.cooperativework.utils.toast

class NoticeInteractionFragment: Fragment() {
    inner class EventHandle(data:NoticeInteractionBean.Item.Data){
        fun onClick(view: View){
            "缺少API".toast()
        }
    }
}