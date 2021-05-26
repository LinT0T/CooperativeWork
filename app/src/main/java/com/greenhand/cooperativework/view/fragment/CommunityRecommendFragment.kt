package com.greenhand.cooperativework.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.adapter.CommunityImageAdapter
import com.youth.banner.Banner
import com.youth.banner.BannerConfig

import com.youth.banner.loader.ImageLoader
import com.youth.banner.Transformer


class CommunityRecommendFragment : Fragment() {
//    private var imagePath: ArrayList<String>? = ArrayList()
//    private var imageTitle: ArrayList<String>? = ArrayList()
    private lateinit var mCommunityImageAdapter:CommunityImageAdapter
    private lateinit var mCommunityImageListView:RecyclerView
    private lateinit var mGridLayoutManager:GridLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_community_recommend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBanner(view)
        initImageList(view)

    }

    private fun initImageList(view: View) {
        mCommunityImageListView=view.findViewById(R.id.rv_image)
        mCommunityImageAdapter= CommunityImageAdapter()
        mGridLayoutManager= GridLayoutManager(activity,2)
        mGridLayoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                when(position){
                    0->return 2
                    else->return 1
                }
            }

        }
        mCommunityImageListView.layoutManager=mGridLayoutManager
        mCommunityImageListView.adapter=mCommunityImageAdapter
    }

    private fun initBanner(view: View) {
//        imagePath?.add("http://img.kaiyanapp.com/cf857a6d72e2ab4b7ba6f0ee79f106e0.jpeg?imageMogr2/quality/60/format/jpg")
//        imagePath?.add("http://img.kaiyanapp.com/3301ea081957934e8916b514ba4aa02a.jpeg?imageMogr2/quality/60/format/jpg")
//        imagePath?.add("http://img.kaiyanapp.com/cf857a6d72e2ab4b7ba6f0ee79f106e0.jpeg?imageMogr2/quality/60/format/jpg")
//        imageTitle?.add("1")
//        imageTitle?.add("2")
//        imageTitle?.add("3")
//        banner = view.findViewById(R.id.banner_recommend)
//        //设置样式
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
//        //设置轮播的动画效果
//        banner.setBannerAnimation(Transformer.ZoomOutSlide)
//        //设置图片加载器
//        banner.setImageLoader(MyImageLoader())
//        //轮播图片的文字
//        banner.setBannerTitles(imageTitle)
//        //设置轮播间隔时间
//        banner.setDelayTime(3000);
//        //设置指示器的位置，小点点，居中显示
//        banner.setIndicatorGravity(BannerConfig.CENTER)
//        //设置图片加载地址
//        banner.setImages(imagePath)
//            //开始调用的方法，启动轮播图。
//            .start()
    }

//    private class MyImageLoader : ImageLoader() {
//        override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
//            Glide.with(context.applicationContext)
//                .load(path)
//                .into(imageView)
//        }
//    }
}