package com.greenhand.cooperativework.view.fragment.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.adapter.CommunityFragmentStateAdapter

class CommunityFragment : Fragment() {
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager2: ViewPager2
    private lateinit var mCommunityFragmentStateAdapter:CommunityFragmentStateAdapter
    private var mFragmentList=ArrayList<Fragment>()
    private val mTabNameList=ArrayList<String>()

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
        mTabLayout = view.findViewById(R.id.tl_community)
        mViewPager2 = view.findViewById(R.id.vp_community)
        mFragmentList.add(CommunityRecommendFragment())
        mFragmentList.add(CommunityFollowFragment())
        mCommunityFragmentStateAdapter = CommunityFragmentStateAdapter(requireActivity(), mFragmentList)
        mViewPager2.adapter = mCommunityFragmentStateAdapter
        mTabNameList.add("推荐")
        mTabNameList.add("关注")
        //设置tab标题
        TabLayoutMediator(
            mTabLayout, mViewPager2
        ) { tab, position -> tab.text = mTabNameList[position] }.attach()
    }
}