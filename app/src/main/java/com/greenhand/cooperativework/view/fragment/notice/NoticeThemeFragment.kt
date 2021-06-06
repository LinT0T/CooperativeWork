package com.greenhand.cooperativework.view.fragment.notice

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseSimplifyRecyclerAdapter
import com.greenhand.cooperativework.bean.NoticeThemeBean
import com.greenhand.cooperativework.databinding.ItemNoticeThemeBinding
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.view.fragment.RefreshAndLoad
import com.greenhand.cooperativework.viewmodel.fragment.NoticeThemeViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class NoticeThemeFragment:Fragment(),RefreshAndLoad {
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout

    private var mTabList = ArrayList<NoticeThemeBean.TabInfo.Tab>()
    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mAdapter:BaseSimplifyRecyclerAdapter
    private lateinit var mLayoutManager:LinearLayoutManager

    private lateinit var mView: View

    private val mViewModel = NoticeThemeViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notice_theme,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView = view


        initView()
        observeData()
        loadOrRefreshData()
        initViewModel()

    }

    private fun initView(){
        mSmartRefreshLayout = mView.findViewById(R.id.theme_refresh_layout)
        mRecyclerView = mView.findViewById(R.id.theme_rv)
        mAdapter = BaseSimplifyRecyclerAdapter(0)
        mLayoutManager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.layoutManager = mLayoutManager
        mSmartRefreshLayout.setOnRefreshListener {
            mViewModel.loadData()
        }
    }

    private fun loadOrRefreshData(){
        mViewModel.loadData()
    }

    private fun initViewModel(){
        mViewModel.setRefreshAndLoadTarget(this)

    }

    private fun observeData(){
        mViewModel.tabList.observe(viewLifecycleOwner,{
            mAdapter.addItemCountAndNotifyRefresh(it.size-mAdapter.itemCount)
            mAdapter
                .onBindView<ItemNoticeThemeBinding>(
                R.layout.item_notice_theme,
                { position-> true},
                {binding, holder, position->
                    val tabCache = it[position]
                    binding.tab = tabCache
                    binding.eventHandle = EventHandle(tabCache)

                })
            if(mRecyclerView.adapter == null) {
                mRecyclerView.adapter = mAdapter
            }
        })
    }


    override fun finishRefresh() {
        mSmartRefreshLayout.finishRefresh()
    }

    override fun finishLoad() {
        //没有加载功能。
    }

    class EventHandle(val tab:NoticeThemeBean.TabInfo.Tab){
        fun onItemSingleClick(view: View){
            "模拟进入tab界面。\ntab名："+ tab.name + "\nApiUrl:"+tab.apiUrl.toast()
        }
    }
}