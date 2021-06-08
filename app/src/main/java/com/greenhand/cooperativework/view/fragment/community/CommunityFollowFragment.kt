package com.greenhand.cooperativework.view.fragment.community

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseSimplifyRecyclerAdapter
import com.greenhand.cooperativework.bean.CommunityFollowBean
import com.greenhand.cooperativework.bean.VideoDetailsBean
import com.greenhand.cooperativework.databinding.ItemCommunityFollowBinding
import com.greenhand.cooperativework.utils.TimeUtil
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.view.activity.VideoDetailsActivity
import com.greenhand.cooperativework.view.fragment.RefreshAndLoad
import com.greenhand.cooperativework.viewmodel.fragment.CommunityFollowViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.util.*
import kotlin.collections.ArrayList


class CommunityFollowFragment : Fragment(), RefreshAndLoad {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout
    private var mFollowVideoList = ArrayList<CommunityFollowBean.Data>()
    private lateinit var mAdapter: BaseSimplifyRecyclerAdapter
    private var start = 0//网络请求获取关注视频的起点
    private val num = 10//每次获取关注视频的数量
    private val mViewModel by lazy {
        ViewModelProvider(this).get(CommunityFollowViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_community_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //初始化View
        initView(view)
        //向ViewModel中传入this 用于刷新和加载后的接口回调
        mViewModel.setFragment(this)
        //观察ViewModel数据变化
        observeData()
        //开始加载数据
        startLoadData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(view: View) {
        mRecyclerView = view.findViewById(R.id.rv_community_follow)
        mSmartRefreshLayout = view.findViewById(R.id.rl_recommend_follow)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = BaseSimplifyRecyclerAdapter(0)
        //设置刷新/加载监听
        mSmartRefreshLayout.setOnRefreshListener {
            //刷新时修改item数量为初始值0
            mAdapter.deleteItemCountAndNotifyRefresh(mAdapter.itemCount)
            //因为关注视频是固定的 所以刷新即重新获取第一页的视频
            start = 0
            startLoadData()
            "因为关注视频是固定的 所以刷新即重新获取第一页的视频".toast()
        }
        mSmartRefreshLayout.setOnLoadMoreListener {
            //加载下一页数据
            startLoadData()
        }
    }

    private fun observeData() {
        mViewModel.followVideoList.observe(viewLifecycleOwner, Observer {
            mFollowVideoList = it as ArrayList<CommunityFollowBean.Data>

            /**
             * 将数据传到adapter中
             * item数量为list大小
             */

            //创建时count为0 每次请求回来+10个数据
            mAdapter.addItemCountAndNotifyRefresh(10)

            //若recyclerView中adapter未被初始化 则初始化
            if (mRecyclerView.adapter == null) {
                mRecyclerView.adapter = mAdapter
                //绑定数据
                mAdapter.onBindView<ItemCommunityFollowBinding>(R.layout.item_community_follow,
                    { true },
                    { binding, holder, position ->
                        /**
                         * 由于 刷新时是下拉 屏幕位置会因为拉动回到第0个item 也就是初始位置
                         * 此时因为未知原因 onBindView会被重复调用多次
                         * 所以可能会发生这样的情况:
                         * 刷新时mFollowVideoList被clear了 而此时onBindView正在被调用 导致获取不到数据
                         * 因此进行mFollowVideoList.size>0的判断
                         */
                        if (mFollowVideoList.size > 0) {
                            binding.content = mFollowVideoList[position].content
                            binding.eventHandle = EventHandle(mFollowVideoList[position].content)
                            // 单独处理视频发布时间
                            val time = holder.itemView.findViewById<TextView>(R.id.tv_time)
                            val data = Date()
                            data.time = mFollowVideoList[position].content.data.releaseTime
                            time.text = TimeUtil.getTime(data)
                        }
                    })
            }
        })
    }

    private fun startLoadData() {
        mViewModel.loadData(start.toString(), num.toString(), true)
        start += 10
    }

    /**
     * 事件内部
     * 通过binding绑定到xml中
     */
    inner class EventHandle(val content: CommunityFollowBean.Content) {
        //处理单击事件
        fun onItemSingleClick(view: View) {
            when (view.id) {
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

    private fun initVideoBean(content: CommunityFollowBean.Content): VideoDetailsBean {
        return VideoDetailsBean(
            content.data.title,
            content.data.playUrl,
            content.data.id.toString(),
            content.data.description,
            content.data.consumption.collectionCount,
            content.data.consumption.realCollectionCount,
            content.data.consumption.replyCount,
            content.data.author.icon,
            content.data.author.name,
            content.data.author.description
        )
    }

    override fun finishRefresh() {
        mSmartRefreshLayout.finishRefresh()
    }

    override fun finishLoad() {
        mSmartRefreshLayout.finishLoadMore()
    }
}