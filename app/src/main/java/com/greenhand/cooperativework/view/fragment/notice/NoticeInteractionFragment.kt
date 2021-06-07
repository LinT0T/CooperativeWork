package com.greenhand.cooperativework.view.fragment.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseSimplifyRecyclerAdapter
import com.greenhand.cooperativework.bean.NoticeInteractionBean
import com.greenhand.cooperativework.databinding.ItemNoticeInteractionBinding
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.view.fragment.RefreshAndLoad
import com.greenhand.cooperativework.viewmodel.fragment.NoticeInteractionViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class NoticeInteractionFragment: Fragment(),RefreshAndLoad {
    //RecyclerView相关成员变量
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter:BaseSimplifyRecyclerAdapter
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    //SmartRefreshLayout成员变量
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout
    //ViewModel
    private lateinit var mViewModel:NoticeInteractionViewModel
    //自身View
    private lateinit var mView:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notice_interaction,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        //初始化控件
        initView()
        //初始化ViewModel
        initViewModel(null,null)
        //初始化listener
        initListener()
        //监控数据
        observeData()
        //第一次加载数据
        firstLoad()
    }

    private fun initView(){
        mRecyclerView = mView.findViewById(R.id.interaction_rv)
        mSmartRefreshLayout = mView.findViewById(R.id.interaction_smart_refresh_layout)

        mAdapter = BaseSimplifyRecyclerAdapter(0)

        mLinearLayoutManager = LinearLayoutManager(context)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLinearLayoutManager
    }

    private fun initViewModel(start:Int?,num:Int?){
        mViewModel = NoticeInteractionViewModel()
        mViewModel.setLoadingParam(start,num)
        mViewModel.setRefreshAndLoadTarget(this)
    }
    private fun observeData(){
        mViewModel.mutableItemList.observe(viewLifecycleOwner,
            {
                val newItemCount = it.size
                mAdapter.changeItemCountAndNotifyRefresh(newItemCount)
                mAdapter.onBindView<ItemNoticeInteractionBinding>(
                    R.layout.item_notice_interaction,
                    {position -> true },
                    {binding, holder, position ->
                        binding.data = it[position].data
                        binding.eventHandle = EventHandle(it[position].data)
                    }
                )
            }
        )
    }

    private fun initListener(){
        mSmartRefreshLayout.setOnRefreshListener { refreshData() }.setOnLoadMoreListener { loadMoreData() }
    }

    private fun firstLoad(){
        mViewModel.loadData(NoticeInteractionViewModel.FIRST_LOAD_MODE)
    }

    private fun refreshData(){
        mViewModel.loadData(NoticeInteractionViewModel.REFRESH_MODE)
    }

    private fun loadMoreData(){
        mViewModel.loadData(NoticeInteractionViewModel.LOAD_MORE_MODE)
    }

    inner class EventHandle(data:NoticeInteractionBean.Item.Data){
        fun onClick(view: View){
            "缺少API".toast()
        }
    }

    override fun finishRefresh() {
        mSmartRefreshLayout.finishRefresh()
    }

    override fun finishLoad() {
        mSmartRefreshLayout.finishLoadMore()
    }


}