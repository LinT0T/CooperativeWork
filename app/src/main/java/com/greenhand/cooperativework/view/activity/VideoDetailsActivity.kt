package com.greenhand.cooperativework.view.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.bean.RelevantVideoBean
import com.greenhand.cooperativework.bean.VideoDetailsBean
import com.greenhand.cooperativework.bean.VideoReplyBean
import com.greenhand.cooperativework.databinding.ActivityVideoDetailsBinding
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.viewmodel.activity.VideoDetailsViewModel
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer


class VideoDetailsActivity : GSYBaseActivityDetail<StandardGSYVideoPlayer>() {
    private lateinit var mItemBinding: ActivityVideoDetailsBinding
    private lateinit var mVideoPlayer: StandardGSYVideoPlayer
    private var mVideoBean: VideoDetailsBean? = null
    private val mViewModel by lazy {
        ViewModelProvider(this).get(VideoDetailsViewModel::class.java)
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
            //处理数据变化监听

        })
    }

    private fun initView() {
        mVideoPlayer = findViewById(R.id.video_player)
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
            .setCacheWithPlay(true)
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
        mItemBinding.eventHandle = EventHandle()
    }

    private fun getVideoBean(): VideoDetailsBean? {
        mVideoBean = intent.getParcelableExtra("videoBean")
        return mVideoBean
    }

    /**
     * 事件内部类
     * 通过binding绑定到xml中
     */
    inner class EventHandle() {
        //处理单击事件
        fun onItemSingleClick(view: View) {
            "缺少API".toast()
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
}