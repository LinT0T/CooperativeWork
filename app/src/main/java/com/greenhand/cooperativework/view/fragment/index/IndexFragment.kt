package com.greenhand.cooperativework.view.fragment.index

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseSimplifyRecyclerAdapter
import com.greenhand.cooperativework.databinding.ItemIndexTest3Binding
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
//        mTabLayout = view.findViewById(R.id.tl_index)
//        mSileShow = view.findViewById(R.id.sh_index)
//
//        mFragments.add(IndexDiscoverFragment())
//        mFragments.add(IndexRecommendFragment())
//        mFragments.add(IndexDailyFragment())
//
//        mSileShow.setAdapter(mFragments, requireActivity())
//
//        val tabList = listOf("发现", "推荐", "日报")
//        TabLayoutMediator(mTabLayout, mSileShow.getViewPager2()) { tab, i ->
//            tab.text = tabList[i]
//        }

        /*
        * 以下是样式代码，有问题随时来找我
        * */
        val recycler = view.findViewById<RecyclerView>(R.id.test_recycler)
        recycler.layoutManager = LinearLayoutManager(context)

        val adapter = BaseSimplifyRecyclerAdapter(10)
        recycler.adapter = adapter
        adapter
            .onBindView(R.layout.item_index_test, MyViewHolder1::class.java, { position ->
                position < 3
            }, { holder, position ->
                holder.view.setBackgroundColor(0xFFFFC107.toInt())
            })

            .onBindView(R.layout.item_index_test_2, MyViewHolder2::class.java, { position ->
                position in 3..5
            }, { holder, position ->
                holder.imageView.setBackgroundColor(0xFF2196F3.toInt())
            })

            .onBindView<ItemIndexTest3Binding>(R.layout.item_index_test_3, { position ->
            position in 6..9
            }, { binding, holder, position ->
                binding.indexTestView3.setBackgroundColor(0xFF4CAF50.toInt())
            })
    }

    class MyViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView.findViewById<View>(R.id.index_test_view_1)
    }

    class MyViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<View>(R.id.index_test_view_2)
    }
}