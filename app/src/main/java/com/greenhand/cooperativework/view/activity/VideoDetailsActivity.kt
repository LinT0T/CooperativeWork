package com.greenhand.cooperativework.view.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseSimplifyRecyclerAdapter
import com.greenhand.cooperativework.bean.*
import com.greenhand.cooperativework.databinding.ActivityVideoDetailsBinding
import com.greenhand.cooperativework.databinding.ItemRelevantVideoBinding
import com.greenhand.cooperativework.databinding.ItemVideoDetailsBinding
import com.greenhand.cooperativework.databinding.ItemVideoReplyBinding
import com.greenhand.cooperativework.utils.TimeUtil
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.viewmodel.activity.VideoDetailsViewModel
import com.hanks.htextview.base.HTextView
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import java.util.*
import kotlin.collections.ArrayList


class VideoDetailsActivity : GSYBaseActivityDetail<StandardGSYVideoPlayer>() {
    private lateinit var mItemBinding: ActivityVideoDetailsBinding
    private lateinit var mVideoPlayer: StandardGSYVideoPlayer
    private var mVideoBean: VideoDetailsBean? = null//被点击的视频的详细信息
    private var mRelevantVideoList = ArrayList<RelevantVideoBean.Data>()//相关视频的信息
    private var mVideoReplyList = ArrayList<VideoReplyBean.Data>()//被点击视频的评论
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: BaseSimplifyRecyclerAdapter
    private val mViewModel by lazy {
        ViewModelProvider(this).get(VideoDetailsViewModel::class.java)
    }

    companion object {
        fun startVideoDetailsActivity(context: Context?, videoDetailsBean: VideoDetailsBean) {
            //跳转至视频Activity
            val intent = Intent(context, VideoDetailsActivity::class.java)
            //初始化视频Bean类 传入Activity中
            intent.putExtra("videoBean", videoDetailsBean)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //创建Binding 并绑定数据
        createBinding()
        //初始化View
        initView()
        //观察ViewModel数据变化
        observeData()
        //开始加载数据
        startLoadData()
    }

    private fun startLoadData() {
        mViewModel.loadData(mVideoBean?.videoId)
    }

    private fun observeData() {
        mViewModel.videoMessageList.observe(this, Observer {
            mRelevantVideoList = it[0] as ArrayList<RelevantVideoBean.Data>
            mVideoReplyList = it[1] as ArrayList<VideoReplyBean.Data>

            //将数据传到adapter中
            mRecyclerView.layoutManager = LinearLayoutManager(this)

            //item数量为1个该视频详细界面+一些相关视频+1个TextView-->"最新评论"+该视频评论
            mAdapter =
                BaseSimplifyRecyclerAdapter(1 + mRelevantVideoList.size + 1 + mVideoReplyList.size)

            //绑定数据
            mRecyclerView.adapter = mAdapter
            mAdapter
                .onBindView<ItemVideoDetailsBinding>(R.layout.item_video_details,
                    { position -> position == 0 },
                    { binding, holder, position ->
                        binding.data = mVideoBean
                        binding.eventHandle = EventHandle(null)
                        //设置TextView展示动画
                        val videoDescription: HTextView =
                            holder.itemView.findViewById(R.id.tv_video_description)
                        videoDescription.animateText(mVideoBean?.videoDescription)
                    })
                .onBindView<ItemRelevantVideoBinding>(R.layout.item_relevant_video,
                    { position -> position in 1 until mRelevantVideoList.size + 1 },
                    { binding, holder, position ->
                        binding.data = mRelevantVideoList[position - 1]
                        binding.eventHandle = EventHandle(mRelevantVideoList[position - 1])
                        //单独处理时间
                        val durationTextView: TextView =
                            holder.itemView.findViewById(R.id.tv_video_duration)
                        durationTextView.text =
                            TimeUtil.getTimeBySecond(mRelevantVideoList[position - 1].duration)
                    })
                .onBindView(
                    R.layout.item_new_reply,
                    EmptyViewHolder::class.java,
                    { position -> position == 1 + mRelevantVideoList.size },
                    { holder, position ->
                        //不需要处理
                    })
                .onBindView<ItemVideoReplyBinding>(R.layout.item_video_reply,
                    { position -> position in 1 + 1 + mRelevantVideoList.size until mVideoReplyList.size + 1 + 1 + mRelevantVideoList.size },
                    { binding, holder, position ->
                        binding.data = mVideoReplyList[position - 1 - 1 - mRelevantVideoList.size]
                        binding.eventHandle = EventHandle(null)
                        //单独处理时间
                        val time: TextView = holder.itemView.findViewById(R.id.tv_reply_time)
                        val date = Date()
                        date.time =
                            mVideoReplyList[position - 1 - 1 - mRelevantVideoList.size].createTime
                        time.text = TimeUtil.getTime(date)
                    })
        })
    }

    private fun initView() {
        mVideoPlayer = findViewById(R.id.video_player)
        mRecyclerView = findViewById(R.id.rv_video_details)
        //初始化player
        initVideoBuilderMode()
        mVideoPlayer.startPlayLogic()
    }

    override fun getGSYVideoPlayer(): StandardGSYVideoPlayer {
        return mVideoPlayer
    }

    override fun getGSYVideoOptionBuilder(): GSYVideoOptionBuilder {
        return GSYVideoOptionBuilder()
            .setUrl(mVideoBean?.videoUrl)
            .setVideoTitle(mVideoBean?.videoTitle)
            .setIsTouchWiget(true)
            .setAutoFullWithSize(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setSeekRatio(1f)
    }

    private fun createBinding() {
        mItemBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_details)
        mItemBinding.videoBean = getVideoBean()
        mItemBinding.eventHandle = EventHandle(null)
    }

    private fun getVideoBean(): VideoDetailsBean? {
        mVideoBean = intent.getParcelableExtra("videoBean")
        return mVideoBean
    }

    /**
     * 事件内部类
     * 通过binding绑定到xml中
     */
    inner class EventHandle(val data: RelevantVideoBean.Data?) {
        //处理单击事件
        fun onItemSingleClick(view: View) {
            when (view.id) {
                R.id.layout_relevant_video -> {
                    //启动视频详细界面
                    startVideoDetailsActivity(this@VideoDetailsActivity, initVideoBean(data!!))
                }
                R.id.iv_replay, R.id.et_reply -> {
                    //点击回复 跳转到回复item
                    if (mVideoReplyList.size != 0) {
                        //若有回复 则跳转到第三个回复item
                        mRecyclerView.scrollToPosition(1 + mRelevantVideoList.size + 3)
                    } else {
                        //若无回复 则跳转到布局最下方
                        mRecyclerView.scrollToPosition(1 + mRelevantVideoList.size)
                    }
                }
                else -> {
                    "缺少API".toast()
                }
            }
        }
    }

    override fun clickForFullScreen() {
    }

    override fun getDetailOrientationRotateAuto(): Boolean {
        return true
    }

    override fun onPause() {
        super.onPause()
        mVideoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        mVideoPlayer.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        if (orientationUtils != null) orientationUtils.releaseListener()
    }

    override fun onBackPressed() {
        //判断视频是否为全屏 若为全屏则先返回正常状态
        if (orientationUtils.screenType == ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR) {
            mVideoPlayer.fullscreenButton.performClick()
            return
        }
        //若视频不为全屏 则释放所有 并super 退出activity
        mVideoPlayer.setVideoAllCallBack(null)
        super.onBackPressed()
    }

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    private fun initVideoBean(data: RelevantVideoBean.Data): VideoDetailsBean {
        return VideoDetailsBean(
            data.title,
            data.playUrl,
            data.id.toString(),
            data.description,
            data.consumption.collectionCount,
            data.consumption.realCollectionCount,
            data.consumption.replyCount,
            data.author.icon,
            data.author.name,
            data.author.description
        )
    }

}