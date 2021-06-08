package com.greenhand.cooperativework.adapter

import com.greenhand.cooperativework.base.BaseDataBindRecyclerAdapter
import com.greenhand.cooperativework.bean.NoticeMessageBean
import com.greenhand.cooperativework.databinding.ItemNoticeMessageBinding
import com.greenhand.cooperativework.view.fragment.RefreshAndLoad

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