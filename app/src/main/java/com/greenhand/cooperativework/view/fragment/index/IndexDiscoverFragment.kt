package com.greenhand.cooperativework.view.fragment.index

import android.view.animation.AccelerateDecelerateInterpolator
import com.bumptech.glide.Glide
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseBindingVMFragment
import com.greenhand.cooperativework.databinding.IndexDiscoverBinding
import com.greenhand.cooperativework.viewmodel.fragment.IndexDiscoverViewModel
import com.ndhzs.slideshow.viewpager2.transformer2.BackgroundToForegroundTransformer

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/30
 */
class IndexDiscoverFragment : BaseBindingVMFragment<IndexDiscoverViewModel, IndexDiscoverBinding>(
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
        val slideShow = mBinding.shIndexDiscoverBanner
        val bannerBean = mViewModel.banner
        bannerBean.observe(this) { list ->
            mBinding.srlIndexDiscover.finishLoadMore()
            val urlList = ArrayList<String>(list.size)
            list.forEach {
                urlList.add(it.data.image)
            }
            if (slideShow.getViewPager2().adapter != null) {
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

    private fun initClassify() {
        val rvClassify = mBinding.rvIndexDiscoverClassify

    }

    private fun initSpecial() {
    }

    private fun initRankList() {
    }

    private fun initTheme() {
    }
}