package com.greenhand.cooperativework.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseDataBindRecyclerAdapter
import com.greenhand.cooperativework.bean.CommunityFirstRecommendBean
import com.greenhand.cooperativework.bean.CommunityRecommendBean
import com.greenhand.cooperativework.bean.CommunityRecommendImageBean
import com.greenhand.cooperativework.databinding.ItemCommunityImageBinding
import com.greenhand.cooperativework.databinding.ItemCommunityVideoBinding
import com.greenhand.cooperativework.utils.TimeUtil
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.view.activity.ImageDetailsActivity
import com.ndhzs.slideshow.SlideShow
import com.ndhzs.slideshow.viewpager2.transformer.AlphaPageTransformer
import com.ndhzs.slideshow.viewpager2.transformer.ScaleInTransformer
import com.ndhzs.slideshow.viewpager2.transformer.ZoomOutPageTransformer
import java.util.*
import kotlin.collections.ArrayList


class CommunityRecommendListAdapter(layoutId: Int, val context: Context?) :
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
    ) { //绑定图片相关数据
        binding.content = mImageList[position - 1 - position / 7]
        //绑定点击事件
        binding.eventHandle = EventHandle(mImageList[position - 1 - position / 7])
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
                TODO()
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
        slideShow.addTransformer(ScaleInTransformer()) // 设置移动动画
            .addTransformer(AlphaPageTransformer())
            .setAutoSlideEnabled(true) // 开启自动滑动
            .setStartItem(1) // 设置起始位置
            .setDelayTime(5000) // 设置自动滚动时间
            .setTimeInterpolator(AccelerateDecelerateInterpolator()) // 设置插值器
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

    /**
     * 事件内部类
     * 通过binding绑定到xml中
     */
    inner class EventHandle(val content: CommunityRecommendBean.Content) {
        //处理单击事件
        fun onItemSingleClick(view: View) {
            when (view.id) {
                R.id.iv_community_image -> {
                    //跳转至图片Activity
                    val intent = Intent(context, ImageDetailsActivity::class.java)
                    //初始化图片Bean类 传入Activity中
                    intent.putExtra("imageBean", initImageBean(content))
                    context?.startActivity(intent)
                }
                else -> {
                    "缺少API".toast()
                }
            }

        }
    }

    private fun initImageBean(content: CommunityRecommendBean.Content): CommunityRecommendImageBean {
        return CommunityRecommendImageBean(
            content.data.owner.avatar,
            content.data.owner.nickname,
            content.data.city,
            content.data.description,
            content.data.consumption.collectionCount,
            content.data.consumption.realCollectionCount,
            content.data.consumption.replyCount,
            content.data.urls
        )
    }
}