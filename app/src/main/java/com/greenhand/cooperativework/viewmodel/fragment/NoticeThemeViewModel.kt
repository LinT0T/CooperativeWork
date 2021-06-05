package com.greenhand.cooperativework.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.greenhand.cooperativework.bean.NoticeThemeBean
import com.greenhand.cooperativework.http.NoticeApi
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.view.fragment.RefreshAndLoad
import okhttp3.*
import java.io.IOException

/**
 * @author XSH
 */

class NoticeThemeViewModel: ViewModel() {

    private lateinit var mRefreshAndLoadTarget:RefreshAndLoad
    private var mTabList = ArrayList<NoticeThemeBean.TabInfo.Tab>()

    private var firstLoad = true
    private var needRefreshNotice = false

    val tabList by lazy{ MutableLiveData<ArrayList<NoticeThemeBean.TabInfo.Tab>>() }

    fun setRefreshAndLoadTarget(refreshAndLoad: RefreshAndLoad){
        mRefreshAndLoadTarget = refreshAndLoad
    }


    //加载数据
    fun loadData(){
        //使用OkHttpClient请求数据
        val client = OkHttpClient()
        val mUrl = HttpUrl.parse(NoticeApi.themeUrl)
        val request = Request.Builder().url(mUrl).get().build()
        val call = client.newCall(request)
        call.enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                "请求失败".toast()
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)//请求成功且未被重置，则处理数据
                    handleData(response)
                else
                    "失败：请求被重置".toast()
            }
        })
    }
    //处理数据
    private fun handleData(response: Response){
        val jsonString = response.body()?.string()
        val noticeThemeBean = Gson().fromJson(jsonString,NoticeThemeBean::class.java)
        val tabListCache = noticeThemeBean.tabInfo.tabList as ArrayList<NoticeThemeBean.TabInfo.Tab>
        mTabList = tabListCache
        tabList.postValue(mTabList)
        if(needRefreshNotice)
            noticeRefreshDone()
        else
            needRefreshNotice = true
    }
    //提醒目标刷新完毕
    private fun noticeRefreshDone(){
        mRefreshAndLoadTarget.finishRefresh()
    }


}