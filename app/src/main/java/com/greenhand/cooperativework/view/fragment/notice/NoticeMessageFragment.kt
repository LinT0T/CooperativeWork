package com.greenhand.cooperativework.view.fragment.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenhand.cooperativework.adapter.NoticeMessageListAdapter
import com.greenhand.cooperativework.viewmodel.fragment.NoticeMessageViewModel

class NoticeMessageFragment:Fragment() {
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
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}