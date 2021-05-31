package com.greenhand.cooperativework.view.fragment.index

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.greenhand.cooperativework.R
import com.ndhzs.slideshow.SlideShow


class IndexFragment : Fragment() {

    private lateinit var mTabLayout: TabLayout
    private lateinit var mSileShow: SlideShow
    private var mFragments = ArrayList<Fragment>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_index, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(view: View) {
        mTabLayout = view.findViewById(R.id.tl_index)
        mSileShow = view.findViewById(R.id.sh_index)

        mFragments.add(IndexDiscoverFragment())
        mFragments.add(IndexRecommendFragment())
        mFragments.add(IndexDailyFragment())

        mSileShow.setAdapter(mFragments, requireActivity())

        val tabList = listOf("发现", "推荐", "日报")
        TabLayoutMediator(mTabLayout, mSileShow.getViewPager2()) { tab, i ->
            tab.text = tabList[i]
        }
    }
}