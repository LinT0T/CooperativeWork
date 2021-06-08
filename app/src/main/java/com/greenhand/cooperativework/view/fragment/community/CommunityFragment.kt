package com.greenhand.cooperativework.view.fragment.community

import com.google.android.material.tabs.TabLayoutMediator
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseOnlyBindingFragment
import com.greenhand.cooperativework.databinding.FragmentCommunityBinding

class CommunityFragment : BaseOnlyBindingFragment<FragmentCommunityBinding>(R.layout.fragment_community) {

    override fun init() {
        val slideShow = mBinding.ssCommunity
        val tabLayout = mBinding.tlCommunity

        val fragments = listOf(
            CommunityRecommendFragment(),
            CommunityFollowFragment()
        )
        slideShow.setAdapter(requireActivity(), fragments)

        val tabNames = listOf("推荐", "关注")
        //设置tab标题
        TabLayoutMediator(
            tabLayout, slideShow.getViewPager2()
        ) { tab, position -> tab.text = tabNames[position] }.attach()
    }
}