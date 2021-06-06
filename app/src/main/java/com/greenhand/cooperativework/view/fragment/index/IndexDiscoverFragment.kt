package com.greenhand.cooperativework.view.fragment.index

import android.annotation.SuppressLint
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseBindingVMFragment
import com.greenhand.cooperativework.base.BaseSimplifyRecyclerAdapter
import com.greenhand.cooperativework.bean.IndexDiscoverBean
import com.greenhand.cooperativework.databinding.IndexDiscoverBinding
import com.greenhand.cooperativework.databinding.ItemIndexDiscoverClassifyBinding
import com.greenhand.cooperativework.databinding.ItemIndexDiscoverRanklistBinding
import com.greenhand.cooperativework.databinding.ItemIndexDiscoverThemeBinding
import com.greenhand.cooperativework.viewmodel.fragment.IndexDiscoverViewModel
import com.ndhzs.slideshow.viewpager2.transformer2.BackgroundToForegroundTransformer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/30
 */
class  IndexDiscoverFragment : BaseBindingVMFragment<IndexDiscoverViewModel, IndexDiscoverBinding>(
    R.layout.fragment_index_discover,
    IndexDiscoverViewModel::class.java
) {

    override fun init() {
        initRefresh()
        initBanner()
        initClassify()
        initSpecial()
        initRankList()
        initTheme()
    }

    private fun initRefresh() {
        mBinding.srlIndexDiscover.setOnRefreshListener {
            mViewModel.refresh()
        }
    }

    private fun initBanner() {
        val slideShow = mBinding.ssIndexDiscoverBanner
        mViewModel.banner.observe(this) { list ->
            mBinding.srlIndexDiscover.finishRefresh() // 取消刷新
            if (list != null) {
                val urlList = ArrayList<String>(list.size)
                list.forEach {
                    urlList.add(it.data.image)
                }
                if (slideShow.hasBeenSetAdapter()) {
                    slideShow.notifyImgDataChange(urlList)
                }else {
                    slideShow
                        .addTransformer(BackgroundToForegroundTransformer())
                        .setAutoSlideEnabled(true)
                        .setDelayTime(5000)
                        .setTimeInterpolator(AccelerateDecelerateInterpolator())
                        .setAdapter(urlList) { data, imageView, holder, position ->
                            Glide.with(this).load(data).into(imageView)
                        }
                }
            }
        }
    }

    private val classifyList = ArrayList<IndexDiscoverBean.ItemX>()
    private fun initClassify() {
        mViewModel.classify.observe(this) {
            if (it != null) {
                val rvClassify = mBinding.rvIndexDiscoverClassify
                classifyList.clear()
                classifyList.addAll(it)
                val adapter = rvClassify.adapter
                if (adapter != null) {
                    adapter.notifyDataSetChanged()
                }else {
                    rvClassify.layoutManager =
                        GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
                    rvClassify
                        .adapter = BaseSimplifyRecyclerAdapter(it.size)
                        .onBindView<ItemIndexDiscoverClassifyBinding>(R.layout.item_index_discover_classify,
                            { true },
                            { binding, _, position ->
                                Glide
                                    .with(this)
                                    .load(classifyList[position].data.image)
                                    .into(binding.imgIndexDiscoverClassify)
                                binding
                                    .tvIndexDiscoverClassify
                                    .text = classifyList[position].data.title
                            })
                }
            }
        }
    }

    private fun initSpecial() {
        mViewModel.special.observe(this) {
            if (it != null) {
                Glide
                    .with(this)
                    .load(it[0].data.image)
                    .into(mBinding.img11IndexDiscoverSpecial)
                Glide
                    .with(this)
                    .load(it[1].data.image)
                    .into(mBinding.img21IndexDiscoverSpecial)
                Glide
                    .with(this)
                    .load(it[2].data.image)
                    .into(mBinding.img12IndexDiscoverSpecial)
                Glide
                    .with(this)
                    .load(it[3].data.image)
                    .into(mBinding.img22IndexDiscoverSpecial)
                mBinding
                    .tv11IndexDiscoverSpecial
                    .text = it[0].data.title
                mBinding
                    .tv21IndexDiscoverSpecial
                    .text = it[1].data.title
                mBinding
                    .tv12IndexDiscoverSpecial
                    .text = it[2].data.title
                mBinding
                    .tv22IndexDiscoverSpecial
                    .text = it[3].data.title
            }
        }
    }

    private val rankListData = ArrayList<IndexDiscoverBean.Data>()
    @SuppressLint("SetTextI18n")
    private fun initRankList() {
        mViewModel.rankList.observe(this) {
            if (it != null) {
                val rvRankList = mBinding.rvIndexDiscoverRankList
                rankListData.clear()
                rankListData.addAll(it)
                val adapter = rvRankList.adapter
                if (adapter != null) {
                    adapter.notifyDataSetChanged()
                } else {
                    rvRankList.layoutManager = LinearLayoutManager(context)
                    rvRankList
                        .adapter = BaseSimplifyRecyclerAdapter(it.size)
                        .onBindView<ItemIndexDiscoverRanklistBinding>(R.layout.item_index_discover_ranklist,
                            { true },
                            { binding, _, position ->
                                Glide
                                    .with(this)
                                    .load(rankListData[position].cover.feed)
                                    .into(binding.imgIndexDiscoverRankList)
                                binding
                                    .tvTimeIndexDiscoverRankList
                                    .text = SimpleDateFormat("mm:ss", Locale.CHINA)
                                    .format(rankListData[position].duration * 1000)
                                binding
                                    .tvTitleIndexDiscoverRankList
                                    .text = rankListData[position].title
                                binding
                                    .tvCategoryItemIndexDiscoverRankList
                                    .text = "#${rankListData[position].category}"
                            })
                }
            }
        }
    }

    private val themeList = ArrayList<IndexDiscoverBean.Data>()
    private fun initTheme() {
        mViewModel.theme.observe(this) {
            if (it != null) {
                val rvTheme = mBinding.rvIndexDiscoverTheme
                themeList.clear()
                themeList.addAll(it)
                val adapter = rvTheme.adapter
                if (adapter != null) {
                    adapter.notifyDataSetChanged()
                }else {
                    rvTheme.layoutManager = LinearLayoutManager(context)
                    rvTheme
                        .adapter = BaseSimplifyRecyclerAdapter(it.size)
                        .onBindView<ItemIndexDiscoverThemeBinding>(R.layout.item_index_discover_theme,
                            { true },
                            { binding, holder, position ->
                                Glide
                                    .with(this)
                                    .load(themeList[position].icon)
                                    .into(binding.imgIndexDiscoverTheme)
                                binding.tvTitleIndexDiscoverTheme
                                    .text = themeList[position].title
                                binding
                                    .tvDescribeItemIndexDiscoverTheme
                                    .text = themeList[position].description
                            })
                }
            }
        }
    }
}