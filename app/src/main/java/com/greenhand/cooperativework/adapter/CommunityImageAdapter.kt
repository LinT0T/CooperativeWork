package com.greenhand.cooperativework.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenhand.cooperativework.R
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader

class CommunityImageAdapter : RecyclerView.Adapter<CommunityImageAdapter.ViewHolder>() {

    private val ITEM_COMMUNITY_BANNER = 0
    private val ITEM_COMMUNITY_IMAGE = 1
    override fun getItemCount(): Int {
        return 20
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return ITEM_COMMUNITY_BANNER
            else -> return ITEM_COMMUNITY_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        var view: View
        when (viewType) {
            ITEM_COMMUNITY_BANNER -> {
                view =
                    layoutInflater.inflate(
                        R.layout.item_community_banner,
                        parent,
                        false
                    )

            }
            else -> {
                view =
                    layoutInflater.inflate(
                        R.layout.item_community_image,
                        parent,
                        false
                    )

            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
             var imagePath: ArrayList<String>? = ArrayList()
             var imageTitle: ArrayList<String>? = ArrayList()
            imagePath?.add("http://img.kaiyanapp.com/cf857a6d72e2ab4b7ba6f0ee79f106e0.jpeg?imageMogr2/quality/60/format/jpg")
            imagePath?.add("http://img.kaiyanapp.com/3301ea081957934e8916b514ba4aa02a.jpeg?imageMogr2/quality/60/format/jpg")
            imagePath?.add("http://img.kaiyanapp.com/cf857a6d72e2ab4b7ba6f0ee79f106e0.jpeg?imageMogr2/quality/60/format/jpg")
            imageTitle?.add("1")
            imageTitle?.add("2")
            imageTitle?.add("3")
            //设置样式
            holder.banner?.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
            //设置轮播的动画效果
            holder. banner?.setBannerAnimation(Transformer.ZoomOutSlide)
            //设置图片加载器
            holder. banner?.setImageLoader(CommunityImageAdapter.MyImageLoader())
            //轮播图片的文字
            holder.banner?.setBannerTitles(imageTitle)
            //设置轮播间隔时间
            holder. banner?.setDelayTime(3000);
            //设置指示器的位置，小点点，居中显示
            holder. banner?.setIndicatorGravity(BannerConfig.CENTER)
            //设置图片加载地址
            holder. banner?.setImages(imagePath)
                //开始调用的方法，启动轮播图。
                ?.start()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val description: TextView? = view.findViewById(R.id.tv_description)
         val userName: TextView? = view.findViewById(R.id.tv_user_name)
         val likeNumber: TextView? = view.findViewById(R.id.tv_like_number)
         val communityImage: ImageView? = view.findViewById(R.id.iv_community_image)
         val userIcon: ImageView? = view.findViewById(R.id.iv_user_icon)
         val like: ImageView? = view.findViewById(R.id.iv_like)

         val banner: Banner?=view.findViewById(R.id.banner_recommend)

    }
   private  class MyImageLoader : ImageLoader() {
        override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
            Glide.with(context.applicationContext)
                .load(path)
                .into(imageView)
        }
    }
}