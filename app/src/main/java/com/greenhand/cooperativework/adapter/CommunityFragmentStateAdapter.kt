package com.greenhand.cooperativework.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityFragmentStateAdapter(
    fragmentActivity: FragmentActivity?,
    fragmentList: ArrayList<Fragment>
) :
    FragmentStateAdapter(fragmentActivity!!) {
    private var fragmentList: ArrayList<Fragment> = fragmentList

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}