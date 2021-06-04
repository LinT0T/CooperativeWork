package com.greenhand.cooperativework.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenhand.cooperativework.bean.IndexDiscoverBean
import com.greenhand.cooperativework.repository.fragment.IndexRepository
import com.greenhand.cooperativework.utils.switchMap
import com.greenhand.cooperativework.utils.switchMapLaunch
import com.greenhand.cooperativework.utils.toast

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/1
 */
class IndexDiscoverViewModel : ViewModel() {

    private val refreshTimes = MutableLiveData(0)

    private val discoverBean = switchMapLaunch(refreshTimes) {
        IndexRepository.getIndexDiscoverBean()
    }
    val banner = switchMap(discoverBean) {
        "首页刷新图片".toast()
        discoverBean.value?.itemList?.get(0)?.data?.itemList
    }
    val classify = switchMap(discoverBean) {
        discoverBean.value?.itemList?.get(1)?.data?.itemList
    }
    val special = switchMap(discoverBean) {
        discoverBean.value?.itemList?.get(2)?.data?.itemList
    }
    val rankList = switchMap(discoverBean) {
        val list: ArrayList<IndexDiscoverBean.Data>?
        if (discoverBean.value?.itemList?.get(4)?.data == null) {
            return@switchMap null
        }else {
            list = ArrayList(5)
        }
        for (i in 1..5) {
            val data = discoverBean.value?.itemList?.get(3 + i)?.data!!
            list.add(data)
        }
        list
    }
    val theme = switchMap(discoverBean) {
        val list: ArrayList<IndexDiscoverBean.Data>?
        if (discoverBean.value?.itemList?.get(10)?.data == null) {
            return@switchMap null
        }else {
            list = ArrayList(10)
        }
        for (i in 1..10) {
            val data = discoverBean.value?.itemList?.get(9 + i)?.data!!
            list.add(data)
        }
        list
    }

    fun refresh() {
        refreshTimes.value = refreshTimes.value!! + 1
    }
}