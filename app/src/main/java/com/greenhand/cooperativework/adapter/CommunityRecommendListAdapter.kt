package com.greenhand.cooperativework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseDataBindRecyclerAdapter
import com.greenhand.cooperativework.bean.CommunityFirstRecommendBean
import com.greenhand.cooperativework.bean.CommunityRecommendBean
import com.greenhand.cooperativework.databinding.ItemCommunityImageBinding
import com.greenhand.cooperativework.databinding.ItemCommunityVideoBinding
import com.greenhand.cooperativework.utils.TimeUtil
import com.ndhzs.slideshow.SlideShow
import com.ndhzs.slideshow.viewpager2.transformer.ZoomOutPageTransformer
import java.util.*
import kotlin.collections.ArrayList


class CommunityRecommendListAdapter(layoutId: Int) :
    BaseDataBindRecyclerAdapter<ItemCommunityImageBinding>(layoutId, true) {

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
    ) {     //绑定图片相关数据
        binding.content = mImageList[position - 1 - position / 7]
    }

    override fun getItemCount(): Int {
        //返回图片+视频的size 再加上1给banner
        return mImageList.size + mVideoList.size + 1
    }

    override fun getYourItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_COMMUNITY_BANNER
            else -> {
                if (position % 7 == 0) {
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
        when (viewType) {
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
                return VideoHolder(itemBinding.root, itemBinding)
            }
            else -> {
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
                val videoHolder = holder as VideoHolder
                initVideoData(videoHolder, position)
            }
        }
        super.onYourBindViewHolder(holder, position, viewType)
    }

    class SlideShowHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val slideShow: SlideShow = itemView.findViewById(R.id.recommend_imageShow)
    }

    class VideoHolder(itemView: View, val binding: ItemCommunityVideoBinding) :
        RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.tv_time)
    }

    private fun initSlideShow(slideShowHolder: SlideShowHolder) {
        val slideShow = slideShowHolder.slideShow
        val imagePath = ArrayList<String>()
        if (mSlideShowList.size > 0) {
            imagePath.add(mSlideShowList[0].data.image)
            imagePath.add(mSlideShowList[1].data.image)
            imagePath.add(mSlideShowList[2].data.image)
        }
        slideShow.setTransformer(ZoomOutPageTransformer()) // 设置移动动画
            .setStartItem(1) // 设置起始位置
            .setDelayTime(5000)
            .setAdapter(imagePath) { data, imageView, holder, position ->
                Glide.with(imageView)
                    .load(data)
                    .into(imageView)
            }
    }

    /**
     * 有关视频的相关数据绑定以及数据的处理与设置
     */
    private fun initVideoData(videoHolder: VideoHolder, position: Int) {
        videoHolder.binding.content = mVideoList[position / 7 - 1]
        // 单独处理视频发布时间
        val data = Date()
        data.time = mVideoList[position / 7 - 1].data.releaseTime
        videoHolder.time.text = TimeUtil.getTime(data)
    }

    /**
     * 添加 社区-推荐 界面的相关数据
     */
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