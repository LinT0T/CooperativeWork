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
import com.ndhzs.slideshow.viewpager2.transformer.ScaleInTransformer
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.util.*

class CommunityRecommendFragment : Fragment(), RefreshAndLoad {
    private lateinit var mCommunityImageListView: RecyclerView
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout
    private lateinit var mGridLayoutManager: GridLayoutManager
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
        //?????????View
        initRecommendListView(view)
        //???ViewModel?????????this ???????????????????????????????????????
        mViewModel.setFragment(this)
        //??????ViewModel????????????
        observeData()
        //??????????????????
        startLoadData()
    }

    private fun initRecommendListView(view: View) {
        mCommunityImageListView = view.findViewById(R.id.rv_image)
        mGridLayoutManager = GridLayoutManager(activity, 2)
        mGridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                //?????? 0 ???????????? ???????????????????????? ??????(??????)??????+??????(??????)??????+??????(??????)??????+??????(??????)??????
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

        //????????????
        mSmartRefreshLayout = view.findViewById(R.id.rl_recommend)
        //??????????????????
        mSmartRefreshLayout.setOnLoadMoreListener {
            mViewModel.loadData("????????????")
        }
        //??????????????????
        mSmartRefreshLayout.setOnRefreshListener {
            //???????????????item??????????????????0
            (mCommunityImageListView.adapter as BaseSimplifyRecyclerAdapter).deleteItemCountAndNotifyRefresh((mCommunityImageListView.adapter as BaseSimplifyRecyclerAdapter).itemCount)
            startLoadData()
        }
    }

    private fun startLoadData() {
        mViewModel.loadData("")//?????????nextPageUrl??????
    }

    private fun observeData() {
        mViewModel.recommendList.observe(viewLifecycleOwner) {
            mSlideShowList = it[0] as ArrayList<CommunityFirstRecommendBean.ItemX>
            mImageList = it[1] as ArrayList<CommunityRecommendBean.Content>
            mVideoList = it[2] as ArrayList<CommunityRecommendBean.Content>

            val adapter = mCommunityImageListView.adapter
            if (adapter != null) {
                //?????????count???1 ??????????????????+14?????????
                (adapter as BaseSimplifyRecyclerAdapter)
                    .addItemCountAndNotifyRefresh(14)
            }else {
                mCommunityImageListView
                    .adapter = BaseSimplifyRecyclerAdapter(15)
                    .onBindView(
                        R.layout.item_community_banner,
                        SlideShowHolder::class.java,
                        { position -> position == 0 },
                        { holder, position ->
                            initSlideShow(holder)
                        })
                    .onBindView<ItemCommunityImageBinding>(R.layout.item_community_image,
                        { position -> position % 7 != 0 },
                        { binding, holder, position ->
                            /**
                             * ?????? ?????????????????? ????????????????????????????????????0???item ?????????????????????
                             * ???????????????????????? onBindView????????????????????????
                             * ????????????????????????????????????:
                             * ?????????mImageList???clear??? ?????????onBindView??????????????? ????????????????????????
                             * ????????????mImageList.size>0?????????
                             * 151?????????
                             */
                            if (mImageList.size > 0) {
                                //????????????????????????
                                binding.content = mImageList[position - 1 - position / 7]
                                //??????????????????
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
                                // ??????????????????????????????
                                val time: TextView =
                                    holder.itemView.findViewById(R.id.tv_release_time)
                                val data = Date()
                                data.time = mVideoList[position / 7 - 1].data.releaseTime
                                time.text = TimeUtil.getTime(data)
                            }
                        })
            }
        }
    }

    private var mIsFirstLoadSlidShow = true
    private fun initSlideShow(slideShowHolder: SlideShowHolder) {
        val slideShow = slideShowHolder.slideShow
        val imagePath = ArrayList<String>()
        if (mSlideShowList.size > 0) {
            imagePath.add(mSlideShowList[0].data.image)
            imagePath.add(mSlideShowList[1].data.image)
            imagePath.add(mSlideShowList[2].data.image)
        }
        if (mIsFirstLoadSlidShow) {
            mIsFirstLoadSlidShow = false
            slideShow
                .addTransformer(ScaleInTransformer())
                .setAutoSlideEnabled(true)
                .setDelayTime(5000)
                .setTimeInterpolator(AccelerateDecelerateInterpolator())
                .setAdapter(imagePath) { data, imageView, holder, position ->
                    Glide
                        .with(this)
                        .load(data)
                        .into(imageView)
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
     * ???????????????
     * ??????binding?????????xml???
     */
    inner class EventHandle(val content: CommunityRecommendBean.Content) {
        //??????????????????
        fun onItemSingleClick(view: View) {
            when (view.id) {
                R.id.iv_community_image, R.id.tv_image_description -> {
                    //???????????????Activity
                    val intent = Intent(context, ImageDetailsActivity::class.java)
                    //???????????????Bean??? ??????Activity???
                    intent.putExtra("imageBean", initImageBean(content))
                    context?.startActivity(intent)
                }
                R.id.iv_community_video_image, R.id.tv_video_description -> {
                    //????????????????????????
                    VideoDetailsActivity.startVideoDetailsActivity(context, initVideoBean(content))
                }
                else -> {
                    "??????API".toast()
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