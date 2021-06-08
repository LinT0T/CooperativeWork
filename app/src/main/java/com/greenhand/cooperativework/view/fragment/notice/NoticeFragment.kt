package com.greenhand.cooperativework.view.fragment.notice

import com.google.android.material.tabs.TabLayoutMediator
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseOnlyBindingFragment
import com.greenhand.cooperativework.databinding.FragmentNoticeBinding

class NoticeFragment : BaseOnlyBindingFragment<FragmentNoticeBinding>(R.layout.fragment_notice) {

    override fun init(){
        val slideShow = mBinding.ssNotice
        val tabLayout = mBinding.tlNotice

        val fragments = listOf(
            NoticeThemeFragment(),
            NoticeMessageFragment(),
            NoticeInteractionFragment()
        )
        slideShow.setAdapter(requireActivity(), fragments)

        val tabNames = listOf(
            R.string.theme,
            R.string.messages,
            R.string.interaction
        )
        //设置tab标题
        TabLayoutMediator(
            tabLayout, slideShow.getViewPager2()
        ) { tab, position -> tab.text = tabNames[position].toString() }.attach()
    }
}