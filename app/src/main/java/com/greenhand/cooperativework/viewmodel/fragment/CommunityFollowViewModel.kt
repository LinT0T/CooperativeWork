package com.greenhand.cooperativework.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenhand.cooperativework.bean.CommunityFollowBean
import com.greenhand.cooperativework.repository.fragment.CommunityFollowRepository
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.view.fragment.community.CommunityFollowFragment
import retrofit2.Call
import retrofit2.Response

class CommunityFollowViewModel : ViewModel() {
    private var mFollowVideoList = ArrayList<CommunityFollowBean.Data>()
    private lateinit var mCommunityFollowFragment: CommunityFollowFragment
    private val repository by lazy {
        CommunityFollowRepository()
    }

    val followVideoList by lazy {
        MutableLiveData<MutableList<CommunityFollowBean.Data>>()
    }

    fun loadData(start: String, num: String, newest: Boolean) {
        //主要针对刷新时 清空集合中的数据 然后重新获取第一页数据
        if (start == "0") {
            mFollowVideoList.removeAll(mFollowVideoList)
        }
        val relevantVideoResult = repository.getCommunityFollow(start, num, newest)
        relevantVideoResult.enqueue(object : retrofit2.Callback<CommunityFollowBean> {
            override fun onResponse(
                call: Call<CommunityFollowBean>,
                response: Response<CommunityFollowBean>
            ) {
                val result = response.body()
                //总list
                var itemList = result?.itemList
                if (itemList != null) {
                    for (i in itemList.indices) {
                        mFollowVideoList.add(itemList[i].data)
                    }
                    postValue()
                }

                if (start=="0"){
                    mCommunityFollowFragment.finishRefresh()
                }else{
                    mCommunityFollowFragment.finishLoad()
                }
            }
            override fun onFailure(call: Call<CommunityFollowBean>, t: Throwable) {
                "网络错误!".toast()
            }
        })
    }

    private fun postValue() {
        followVideoList.postValue(mFollowVideoList)
    }

    fun setFragment(communityFollowFragment: CommunityFollowFragment) {
        mCommunityFollowFragment = communityFollowFragment
    }
}