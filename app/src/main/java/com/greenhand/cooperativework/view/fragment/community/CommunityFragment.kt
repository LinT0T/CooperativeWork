package com.greenhand.cooperativework.view.fragment.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.greenhand.cooperativework.R
import com.ndhzs.slideshow.SlideShow

class CommunityFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化相关View
        initView(view)
    }
    private fun initView(view: View) {
        val slideShow = view.findViewById<SlideShow>(R.id.ss_community)
        val tabLayout = view.findViewById<TabLayout>(R.id.tl_community)

        val fragments = ArrayList<Fragment>(2)
        fragments.add(CommunityRecommendFragment())
        fragments.add(CommunityFollowFragment())
        slideShow.setAdapter(fragments, requireActivity())

        val tabNames = listOf("推荐", "关注")
        //设置tab标题
        TabLayoutMediator(
            tabLayout, slideShow.getViewPager2()
        ) { tab, position -> tab.text = tabNames[position] }.attach()
    }
}