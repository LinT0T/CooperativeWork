package com.greenhand.cooperativework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseDataBindRecyclerAdapter
import com.greenhand.cooperativework.bean.NoticeMessageBean
import com.greenhand.cooperativework.bean.NoticeMessageFragmentBean
import com.greenhand.cooperativework.databinding.ItemNoticeMessageBinding
import java.util.zip.Inflater

class NoticeMessageListAdapter(layoutId: Int) : BaseDataBindRecyclerAdapter<ItemNoticeMessageBinding>(layoutId) {
    private var mMessageBeanList=ArrayList<NoticeMessageFragmentBean>()

    override fun onBaseBindViewHolder(
        binding: ItemNoticeMessageBinding,
        holder: BaseDataBindViewHolder,
        position: Int
    ) {
        binding.message = mMessageBeanList[position]
    }

    override fun getItemCount(): Int {
        return mMessageBeanList.size
    }

    override fun onYourCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder? {
        //只有一种默认的ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notice_message,parent,false)
        return DefaultMessageViewHolder(view)
    }

    override fun onYourBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        viewType: Int
    ) {
        val defaultHolder = holder as DefaultMessageViewHolder
        initDefaultHolder(defaultHolder,position)
    }

    private class DefaultMessageViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val titleTv: TextView = itemView.findViewById(R.id.message_title_tv)
        val contentTv:TextView = itemView.findViewById(R.id.message_content_tv)
        val dateTv:TextView = itemView.findViewById(R.id.message_date_tv)
        val viewedStateTv:TextView = itemView.findViewById(R.id.message_viewed_tv)
        val iconIv: ImageView = itemView.findViewById(R.id.message_icon_img_view)
    }

    fun setData(messageBeanList:ArrayList<NoticeMessageFragmentBean>){
        mMessageBeanList = messageBeanList
    }

    private fun initDefaultHolder(holder:DefaultMessageViewHolder,position: Int){
        val messageData = mMessageBeanList[position]
        holder.titleTv.text = messageData.title
        holder.contentTv.text = messageData.content
        holder.viewedStateTv.text = messageData.viewStateString
        //date转换还没写好，暂时用毫秒数
        holder.dateTv.text = messageData.date.toString()+"毫秒前"

    }

}