package com.greenhand.cooperativework.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseDataBindRecyclerAdapter
import com.greenhand.cooperativework.bean.NoticeMessageBean
import com.greenhand.cooperativework.databinding.ItemNoticeMessageBinding
import com.greenhand.cooperativework.view.fragment.RefreshAndLoad
import java.util.zip.Inflater

class NoticeMessageListAdapter(layoutId: Int) : BaseDataBindRecyclerAdapter<ItemNoticeMessageBinding>(layoutId,false) {
    private var mMessageBeanList=ArrayList<NoticeMessageBean>()

    override fun onBaseBindViewHolder(
        binding: ItemNoticeMessageBinding,
        holder: BaseDataBindViewHolder,
        position: Int
    ) {
        binding.message = mMessageBeanList[position]
        binding.eventHandle = mMessageBeanList[position].EventHandle()
    }

    override fun getItemCount(): Int {
        return mMessageBeanList.size
    }


    fun setData(messageBeanList:ArrayList<NoticeMessageBean>){
        mMessageBeanList = messageBeanList
        this.notifyDataSetChanged()
    }


    inner class EventHandle{
        fun onItemSingleClick(){

        }
    }

    fun setFragment(refreshAndLoad: RefreshAndLoad){

    }
}