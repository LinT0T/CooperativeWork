package com.ndhzs.slideshow.viewpager2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/28
 */
abstract class BaseFragmentStateAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}