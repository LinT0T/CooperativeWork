package com.greenhand.cooperativework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseDataBindRecyclerAdapter
import com.greenhand.cooperativework.bean.CommunityFirstRecommendBean
import com.greenhand.cooperativework.bean.CommunityRecommendBean
import com.greenhand.cooperativework.databinding.ItemCommunityImageBinding
import com.greenhand.cooperativework.databinding.ItemCommunityVideoBinding
import com.ndhzs.slideshow.SlideShow
import com.ndhzs.slideshow.viewpager2.transformer.ZoomOutPageTransformer

class CommunityRecommendListAdapter(layoutId: Int):
    BaseDataBindRecyclerAdapter<ItemCommunityImageBinding>(layoutId,true) {

    companion object {
        private const val ITEM_COMMUNITY_BANNER = 0
        private const val ITEM_COMMUNITY_VIDEO = 2
    }

    private var mSlideShowList = ArrayList<CommunityFirstRecommendBean.ItemX>()
    private var mImageList = ArrayList<CommunityRecommendBean.Content>()
    private var mVideoList = ArrayList<CommunityRecommendBean.Content>()

    //数据集合
    private val mContentList by lazy {
        mutableListOf<Any>()
    }

    override fun onBaseBindViewHolder(
        binding: ItemCommunityImageBinding,
        holder: BaseDataBindViewHolder,
        position: Int
    ) {
        /*
        * 在这里是你的 ImageBinding
        * */
        binding.content = mImageList[position - 1]
    }

    override fun getItemCount(): Int {
        //返回图片+视频的size 再加上1给banner
        return mImageList.size + mVideoList.size + 1
    }

    override fun getYourItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_COMMUNITY_BANNER
            else -> {
                if (position % 13 == 0) {
                    ITEM_COMMUNITY_VIDEO
                } else {
                    BASE_VIEW_HOLDER
                }
            }
        }
    }

    override fun onYourCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder? {
       when (viewType){
           ITEM_COMMUNITY_BANNER -> {
               // 这里没有必要使用 DataBinding
               val view = LayoutInflater.from(parent.context)
                   .inflate(R.layout.item_community_banner, parent, false)
               return SlideShowHolder(view)
           }
           ITEM_COMMUNITY_VIDEO -> {
               val itemBinding = DataBindingUtil.inflate<ItemCommunityVideoBinding>(
                   LayoutInflater.from(parent.context),
                   R.layout.item_community_video,
                   parent,
                   false
               )
               return VideoHolder(itemBinding.root)
           }
           else -> {
               // 已经将你写的关于 ImageBinding 的东西放在了第40行的 onBaseBindViewHolder 方法中
               return null
           }
       }
    }

    override fun onYourBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        viewType: Int
    ) {

        when (viewType) {
            ITEM_COMMUNITY_BANNER -> {
                val slideShowHolder = holder as SlideShowHolder
                initSlideShow(slideShowHolder)
            }
            ITEM_COMMUNITY_VIDEO -> {
                // 建议写多个 ViewHolder，防止出现 findViewById 的错误
                val videoHolder = holder as VideoHolder
            }
        }
        super.onYourBindViewHolder(holder, position, viewType)
    }

    class SlideShowHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val slideShow: SlideShow = itemView.findViewById(R.id.recommend_imageShow)
    }

    class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    private fun initSlideShow(slideShowHolder: SlideShowHolder) {
        val slideShow = slideShowHolder.slideShow
        val imagePath = ArrayList<String>()
        imagePath.add("http://img.kaiyanapp.com/cf857a6d72e2ab4b7ba6f0ee79f106e0.jpeg?imageMogr2/quality/60/format/jpg")
        imagePath.add("http://img.kaiyanapp.com/3301ea081957934e8916b514ba4aa02a.jpeg?imageMogr2/quality/60/format/jpg")
        imagePath.add("http://img.kaiyanapp.com/cf857a6d72e2ab4b7ba6f0ee79f106e0.jpeg?imageMogr2/quality/60/format/jpg")

        slideShow.setTransformer(ZoomOutPageTransformer()) // 设置移动动画
            .setStartItem(1) // 设置起始位置
            .setDelayTime(5000)
            .setAdapter(imagePath) { data, imageView, holder, position ->
                Glide.with(imageView)
                    .load(data)
                    .into(imageView)
            }
    }

    fun setData(contentList: MutableList<Any>) {
        //清空并添加新内容内容
        mContentList.run {
            clear()
            addAll(contentList)
            mSlideShowList = mContentList[0] as ArrayList<CommunityFirstRecommendBean.ItemX>
            mImageList = mContentList[1] as ArrayList<CommunityRecommendBean.Content>
            mVideoList = mContentList[2] as ArrayList<CommunityRecommendBean.Content>
            notifyDataSetChanged()
        }
    }
}