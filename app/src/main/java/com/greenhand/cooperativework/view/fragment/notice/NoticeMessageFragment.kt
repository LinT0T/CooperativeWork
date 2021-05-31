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
import com.greenhand.cooperativework.viewmodel.fragment.NoticeMessageViewModel

class NoticeMessageFragment:Fragment() {
    private lateinit var mView:View
    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mMessageListAdapter: NoticeMessageListAdapter
    private lateinit var mLayoutManager:LinearLayoutManager
    private lateinit var mMessageViewModel:NoticeMessageViewModel

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
        firstLoadData()
    }

    private fun initViews(){
        mRecyclerView = mView.findViewById(R.id.message_rv)
        mLayoutManager = LinearLayoutManager(context)
        mMessageListAdapter = NoticeMessageListAdapter(R.layout.item_notice_message)
        mRecyclerView.adapter = mMessageListAdapter
        mRecyclerView.layoutManager = mLayoutManager
    }

    private fun initViewModel(){
        mMessageViewModel = NoticeMessageViewModel()
        mMessageViewModel.messageList.observe(viewLifecycleOwner,
            {
                mMessageListAdapter.setData(it)
            }
        )
    }


    private fun firstLoadData(){
        mMessageViewModel.loadData()
    }


}