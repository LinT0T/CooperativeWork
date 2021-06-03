package com.greenhand.cooperativework.view.fragment.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseSimplifyRecyclerAdapter
import com.greenhand.cooperativework.bean.CommunityFirstRecommendBean
import com.greenhand.cooperativework.bean.CommunityRecommendBean
import com.greenhand.cooperativework.bean.CommunityRecommendImageBean
import com.greenhand.cooperativework.bean.VideoDetailsBean
import com.greenhand.cooperativework.databinding.*
import com.greenhand.cooperativework.utils.TimeUtil
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.view.activity.ImageDetailsActivity
import com.greenhand.cooperativework.view.activity.VideoDetailsActivity
import com.greenhand.cooperativework.view.fragment.RefreshAndLoad
import com.greenhand.cooperativework.viewmodel.fragment.CommunityRecommendViewModel
import com.ndhzs.slideshow.SlideShow
import com.ndhzs.slideshow.viewpager2.transformer.AlphaPageTransformer
import com.ndhzs.slideshow.viewpager2.transformer.ScaleInTransformer
import com.ndhzs.slideshow.viewpager2.transformer2.BackgroundToForegroundTransformer
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.util.*

class CommunityRecommendFragment : Fragment(), RefreshAndLoad {
    private lateinit var mCommunityImageListView: RecyclerView
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout
    private lateinit var mGridLayoutManager: GridLayoutManager
    private lateinit var mAdapter: BaseSimplifyRecyclerAdapter
    private var mSlideShowList = ArrayList<CommunityFirstRecommendBean.ItemX>()
    private var mImageList = ArrayList<CommunityRecommendBean.Content>()
    private var mVideoList = ArrayList<CommunityRecommendBean.Content>()
    private val mViewModel by lazy {
        ViewModelProvider(this).get(CommunityRecommendViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community_recommend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化View
        initRecommendListView(view)
        //向ViewModel中传入this 用于刷新和加载后的接口回调
        mViewModel.setFragment(this)
        //观察ViewModel数据变化
        observeData()
        //开始加载数据
        startLoadData()
    }

    private fun initRecommendListView(view: View) {
        mCommunityImageListView = view.findViewById(R.id.rv_image)
        mGridLayoutManager = GridLayoutManager(activity, 2)
        mGridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                //位置 0 为轮播图 之后一个周期包含 三行(六张)图片+一行(一个)视频+三行(六张)图片+一行(一个)视频
                return when (position) {
                    0 -> 2
                    else -> {
                        if (position % 7 == 0) {
                            2
                        } else {
                            1
                        }
                    }
                }
            }
        }
        mCommunityImageListView.layoutManager = mGridLayoutManager

        //itemCount每一周期为6个图片+1个视频+6个图片+1个视频 首次仅创建1个banner的count observeData()中会每次请求使count+14
        mAdapter = BaseSimplifyRecyclerAdapter(1)

        //配置加载
        mSmartRefreshLayout = view.findViewById(R.id.rl_recommend)
        //设置加载更多
        mSmartRefreshLayout.setOnLoadMoreListener {
            mViewModel.loadData("加载更多")
        }
        //设置下拉刷新
        mSmartRefreshLayout.setOnRefreshListener {
            //刷新则重新创建adapter
            mAdapter = BaseSimplifyRecyclerAdapter(1)
            startLoadData()
        }
    }

    private fun startLoadData() {
        mViewModel.loadData("")//第一页nextPageUrl为空
    }

    private fun observeData() {
        mViewModel.recommendList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mSlideShowList = it[0] as ArrayList<CommunityFirstRecommendBean.ItemX>
            mImageList = it[1] as ArrayList<CommunityRecommendBean.Content>
            mVideoList = it[2] as ArrayList<CommunityRecommendBean.Content>
            //创建时count为1 每次请求回来+14个数据
            mAdapter.addItemCount(14)

            mAdapter
                .onBindView(
                    R.layout.item_community_banner,
                    SlideShowHolder::class.java,
                    { position -> position == 0 },
                    { holder, position ->
                        initSlideShow(holder)
                    })
                .onBindView<ItemCommunityImageBinding>(R.layout.item_community_image,
                    { position -> position != 0 && position % 7 != 0 },
                    { binding, holder, position ->
                        /**
                         * 由于 刷新时是下拉 屏幕位置会因为拉动回到第0个item 也就是初始位置
                         * 此时因为未知原因 onBindView会被重复调用多次
                         * 所以可能会发生这样的情况:
                         * 刷新时mImageList被clear了 而此时onBindView正在被调用 导致获取不到数据
                         * 因此进行mImageList.size>0的判断
                         * 151行同理
                         */
                        if (mImageList.size > 0) {
                            //绑定图片相关数据
                            binding.content = mImageList[position - 1 - position / 7]
                            //绑定点击事件
                            binding.eventHandle =
                                EventHandle(mImageList[position - 1 - position / 7])
                        }
                    })
                .onBindView<ItemCommunityVideoBindingImpl>(R.layout.item_community_video,
                    { position -> position != 0 && position % 7 == 0 },
                    { binding, holder, position ->
                        if (mVideoList.size > 0) {
                            binding.content = mVideoList[position / 7 - 1]
                            binding.eventHandle = EventHandle(mVideoList[position / 7 - 1])
                            // 单独处理视频发布时间
                            val time: TextView =
                                holder.itemView.findViewById(R.id.tv_release_time)
                            val data = Date()
                            data.time = mVideoList[position / 7 - 1].data.releaseTime
                            time.text = TimeUtil.getTime(data)
                        }
                    })

            //若adapter中itemCount数量满足最初页的count 即15 则初始化recyclerView中的adapter
            if (mAdapter.itemCount == 15) {
                mCommunityImageListView.adapter = mAdapter
            }
        })
    }

    private fun initSlideShow(slideShowHolder: SlideShowHolder) {
        val slideShow = slideShowHolder.slideShow
        val imagePath = ArrayList<String>()
        if (mSlideShowList.size > 0) {
            imagePath.add(mSlideShowList[0].data.image)
            imagePath.add(mSlideShowList[1].data.image)
            imagePath.add(mSlideShowList[2].data.image)
        }
//        slideShow.addTransformer(ScaleInTransformer()) // 设置移动动画
//            .addTransformer(AlphaPageTransformer())
//            .setAutoSlideEnabled(true) // 开启自动滑动
//            .setDelayTime(5000) // 设置自动滚动时间，但目前还没有实现自动滚动
//            .setTimeInterpolator(AccelerateDecelerateInterpolator())
//            .setAdapter(imagePath) { data, imageView, holder, position ->
//                Glide.with(imageView)
//                    .load(data)
//                    .into(imageView)
//            }
        if (slideShow.getViewPager2().adapter != null) {
            slideShow.notifyImgDataChange(imagePath)
        } else {
            slideShow
                .addTransformer(ScaleInTransformer())
                .setAutoSlideEnabled(true)
                .setDelayTime(5000)
                .setTimeInterpolator(AccelerateDecelerateInterpolator())
                .setAdapter(imagePath) { data, imageView, holder, position ->
                    Glide.with(this).load(data).into(imageView)
                }
        }
    }

    override fun finishRefresh() {
        mSmartRefreshLayout.finishRefresh()
    }

    override fun finishLoad() {
        mSmartRefreshLayout.finishLoadMore()
    }

    /**
     * 事件内部类
     * 通过binding绑定到xml中
     */
    inner class EventHandle(val content: CommunityRecommendBean.Content) {
        //处理单击事件
        fun onItemSingleClick(view: View) {
            when (view.id) {
                R.id.iv_community_image, R.id.tv_image_description -> {
                    //跳转至图片Activity
                    val intent = Intent(context, ImageDetailsActivity::class.java)
                    //初始化图片Bean类 传入Activity中
                    intent.putExtra("imageBean", initImageBean(content))
                    context?.startActivity(intent)
                }
                R.id.iv_community_video_image, R.id.tv_video_description -> {
                    //启动视频详细界面
                    VideoDetailsActivity.startVideoDetailsActivity(context, initVideoBean(content))
                }
                else -> {
                    "缺少API".toast()
                }
            }

        }
    }

    class SlideShowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slideShow: SlideShow = itemView.findViewById(R.id.recommend_imageShow)
    }

    private fun initVideoBean(content: CommunityRecommendBean.Content): VideoDetailsBean {
        return VideoDetailsBean(
            content.data.title,
            content.data.playUrl,
            content.data.id.toString(),
            content.data.description,
            content.data.consumption.collectionCount,
            content.data.consumption.realCollectionCount,
            content.data.consumption.replyCount,
            content.data.owner.avatar,
            content.data.owner.nickname,
            content.data.owner.description.toString()
        )
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