package com.greenhand.cooperativework.view.fragment.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.adapter.NoticeMessageListAdapter
import com.greenhand.cooperativework.view.fragment.RefreshAndLoad
import com.greenhand.cooperativework.viewmodel.fragment.NoticeMessageViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class NoticeMessageFragment:Fragment(),RefreshAndLoad {
    private lateinit var mView:View
    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mMessageListAdapter: NoticeMessageListAdapter
    private lateinit var mLayoutManager:LinearLayoutManager
    private lateinit var mMessageViewModel:NoticeMessageViewModel
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notice_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        initViews()
        initViewModel()
        loadData()
    }

    private fun initViews(){
        mRecyclerView = mView.findViewById(R.id.message_rv)
        mLayoutManager = LinearLayoutManager(context)
        mMessageListAdapter = NoticeMessageListAdapter(R.layout.item_notice_message)
        mRecyclerView.adapter = mMessageListAdapter
        mRecyclerView.layoutManager = mLayoutManager
        mSmartRefreshLayout = mView.findViewById(R.id.message_smart_refresh_layout)
        mSmartRefreshLayout.setOnLoadMoreListener{
            loadData()
        }
        mSmartRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun initViewModel(){
        mMessageViewModel = NoticeMessageViewModel()
        mMessageViewModel.setRefreshAndLoad(this)
        mMessageViewModel.messageList.observe(viewLifecycleOwner,
            {
                mMessageListAdapter.setData(it)
            }
        )
    }

    private fun refreshData(){
        mMessageViewModel.refreshData()
    }

    private fun loadData(){
        mMessageViewModel.loadData()
    }

    override fun finishRefresh() {
        mSmartRefreshLayout.finishRefresh()
    }

    override fun finishLoad() {
        mSmartRefreshLayout.finishLoadMore()
    }

}