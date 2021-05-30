package com.greenhand.cooperativework.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.adapter.CommunityRecommendListAdapter
import com.greenhand.cooperativework.viewmodel.fragment.CommunityRecommendViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout


class CommunityRecommendFragment : Fragment(), RefreshAndLoad {
    private lateinit var mCommunityRecommendListAdapter: CommunityRecommendListAdapter
    private lateinit var mCommunityImageListView: RecyclerView
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout
    private lateinit var mGridLayoutManager: GridLayoutManager
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
        //向ViewModel中传入this
        mViewModel.setFragment(this)
        //观察ViewModel数据变化
        observeData()
        //开始加载数据
        startLoadData()
    }

    private fun initRecommendListView(view: View) {
        mCommunityImageListView = view.findViewById(R.id.rv_image)
        mCommunityRecommendListAdapter =
            CommunityRecommendListAdapter(R.layout.item_community_image, context)
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
        mCommunityImageListView.adapter = mCommunityRecommendListAdapter

        //配置加载
        mSmartRefreshLayout = view.findViewById(R.id.rl_recommend)
        //设置加载更多
        mSmartRefreshLayout.setOnLoadMoreListener {
            mViewModel.loadData("加载更多")
        }
        //设置下拉刷新
        mSmartRefreshLayout.setOnRefreshListener {
            startLoadData()
        }
    }

    private fun startLoadData() {
        mViewModel.loadData("")//第一页nextPageUrl为空
    }

    private fun observeData() {
        mViewModel.recommendList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            //将数据传到adapter中
            mCommunityRecommendListAdapter.setData(it)
        })
    }

    override fun finishRefresh() {
        mSmartRefreshLayout.finishRefresh()
    }

    override fun finishLoad() {
        mSmartRefreshLayout.finishLoadMore()
    }
}