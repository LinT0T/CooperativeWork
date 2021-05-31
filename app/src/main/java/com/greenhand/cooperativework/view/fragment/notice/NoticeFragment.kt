package com.greenhand.cooperativework.view.fragment.notice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.adapter.NoticeFragmentAdapter

class NoticeFragment : Fragment() {
    private var fragmentList=ArrayList<Fragment>()
    private var nameList= ArrayList<String>()

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    private lateinit var noticeFragmentAdapter:NoticeFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View){
        tabLayout = view.findViewById(R.id.notice_fragment_tab_layout)
        viewPager2 = view.findViewById(R.id.notice_fragment_viewpager2)

        noticeFragmentAdapter = NoticeFragmentAdapter(activity,fragmentList)

        viewPager2.adapter = noticeFragmentAdapter

        fragmentList.add(NoticeThemeFragment())
        fragmentList.add(NoticeMessageFragment())
        fragmentList.add(NoticeInteractionFragment())

        nameList.add(getString(R.string.theme))
        nameList.add(getString(R.string.messages))
        nameList.add(getString(R.string.interaction))

        TabLayoutMediator(tabLayout,viewPager2){
            tab, position ->  tab.text = nameList[position]
        }.attach()
    }


}