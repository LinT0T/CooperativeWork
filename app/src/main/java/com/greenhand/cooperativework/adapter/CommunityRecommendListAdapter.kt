package com.greenhand.cooperativework.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseDataBindRecyclerAdapter
import com.greenhand.cooperativework.bean.CommunityFirstRecommendBean
import com.greenhand.cooperativework.bean.CommunityRecommendBean
import com.greenhand.cooperativework.databinding.ItemCommunityBannerBinding
import com.greenhand.cooperativework.databinding.ItemCommunityImageBinding
import com.greenhand.cooperativework.databinding.ItemCommunityVideoBinding
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader

class CommunityRecommendListAdapter(layoutId:Int):
    BaseDataBindRecyclerAdapter<ViewDataBinding>(layoutId,true) {
    private val ITEM_COMMUNITY_BANNER = 0
    private val ITEM_COMMUNITY_IMAGE = 1
    private val ITEM_COMMUNITY_VIDEO=2
    private  var mBannerList=ArrayList<CommunityFirstRecommendBean.ItemX>()
    private  var mImageList=ArrayList<CommunityRecommendBean.Content>()
    private  var mVideoList=ArrayList<CommunityRecommendBean.Content>()

    //数据集合
    private val mContentList by lazy {
        mutableListOf<Any>()
    }
    override fun onBaseBindViewHolder(
        binding: ViewDataBinding,
        holder: BaseDataBindViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        //返回图片+视频的size 再加上1给banner
        return mImageList.size+mVideoList.size+1
    }

    override fun getYourItemViewType(position: Int): Int {
        when (position) {
            0 -> return ITEM_COMMUNITY_BANNER
            else -> {
                if (position%13==0){
                    return ITEM_COMMUNITY_VIDEO
                }else{
                    return ITEM_COMMUNITY_IMAGE
                }
            }
        }
    }
    override fun onYourCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder? {
       when(viewType){
           ITEM_COMMUNITY_BANNER->{
               val itemBinding = DataBindingUtil.inflate<ItemCommunityBannerBinding>(
                   LayoutInflater.from(parent.context),
                   R.layout.item_community_banner,
                   parent,
                   false
               )
               return InnerHolder(itemBinding.root,itemBinding)
           }
           ITEM_COMMUNITY_IMAGE->{
               val itemBinding = DataBindingUtil.inflate<ItemCommunityImageBinding>(
                   LayoutInflater.from(parent.context),
                   R.layout.item_community_image,
                   parent,
                   false
               )
               return InnerHolder(itemBinding.root,itemBinding)
           }
           else->{
               val itemBinding = DataBindingUtil.inflate<ItemCommunityVideoBinding>(
                   LayoutInflater.from(parent.context),
                   R.layout.item_community_video,
                   parent,
                   false
               )
               return InnerHolder(itemBinding.root,itemBinding)
           }
       }
    }

    override fun onYourBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        viewType: Int
    ) {
        val innerHolder=holder as InnerHolder
        when(viewType){
           ITEM_COMMUNITY_BANNER->{
               initBanner(innerHolder)
           }
           ITEM_COMMUNITY_IMAGE->{
             val binding= innerHolder.binding as ItemCommunityImageBinding
               binding.content=mImageList[position-1]
           }

       }
        super.onYourBindViewHolder(holder, position, viewType)
    }

    class InnerHolder(itemView: View, val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(itemView){
        val banner: Banner?=itemView.findViewById(R.id.banner_recommend)
        }

    private fun initBanner(innerHolder:InnerHolder){
        var imagePath: ArrayList<String>? = ArrayList()
        var imageTitle: ArrayList<String>? = ArrayList()
        imagePath?.add("http://img.kaiyanapp.com/cf857a6d72e2ab4b7ba6f0ee79f106e0.jpeg?imageMogr2/quality/60/format/jpg")
        imagePath?.add("http://img.kaiyanapp.com/3301ea081957934e8916b514ba4aa02a.jpeg?imageMogr2/quality/60/format/jpg")
        imagePath?.add("http://img.kaiyanapp.com/cf857a6d72e2ab4b7ba6f0ee79f106e0.jpeg?imageMogr2/quality/60/format/jpg")
        imageTitle?.add("1")
        imageTitle?.add("2")
        imageTitle?.add("3")
        //设置样式
        innerHolder.banner?.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
        //设置轮播的动画效果
        innerHolder. banner?.setBannerAnimation(Transformer.ZoomOutSlide)
        //设置图片加载器
        innerHolder. banner?.setImageLoader(MyImageLoader())
        //轮播图片的文字
        innerHolder.banner?.setBannerTitles(imageTitle)
        //设置轮播间隔时间
        innerHolder. banner?.setDelayTime(3000);
        //设置指示器的位置，小点点，居中显示
        innerHolder. banner?.setIndicatorGravity(BannerConfig.CENTER)
        //设置图片加载地址
        innerHolder. banner?.setImages(imagePath)
            //开始调用的方法，启动轮播图。
            ?.start()
    }
    fun setData(contentList: MutableList<Any>) {
        //清空并添加新内容内容
        mContentList.run {
            clear()
            addAll(contentList)
            mBannerList= mContentList[0] as ArrayList<CommunityFirstRecommendBean.ItemX>
            mImageList= mContentList[1] as ArrayList<CommunityRecommendBean.Content>
            mVideoList= mContentList[2] as ArrayList<CommunityRecommendBean.Content>
            notifyDataSetChanged()
        }
    }
    private  class MyImageLoader : ImageLoader() {
        override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
            Glide.with(context.applicationContext)
                .load(path)
                .into(imageView)
        }
    }
}