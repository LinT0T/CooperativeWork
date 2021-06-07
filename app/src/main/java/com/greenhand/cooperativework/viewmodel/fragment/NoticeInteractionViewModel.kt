package com.greenhand.cooperativework.viewmodel.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.greenhand.cooperativework.bean.NoticeInteractionBean
import com.greenhand.cooperativework.http.NoticeApi
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.view.fragment.RefreshAndLoad
import okhttp3.*
import java.io.IOException

class NoticeInteractionViewModel:ViewModel() {
    private lateinit var mInteractionBean:NoticeInteractionBean
    private lateinit var mItemList:ArrayList<NoticeInteractionBean.Item>
    private lateinit var mRefreshAndLoadTarget: RefreshAndLoad
    private lateinit var mNextPageUrl:String
    private var start = 0
    private var num = 10
    private var mLoadMode = FIRST_LOAD_MODE

    fun setRefreshAndLoadTarget(refreshAndLoad: RefreshAndLoad){
        mRefreshAndLoadTarget = refreshAndLoad
    }


    fun loadData(loadMode:Int?){
        if(loadMode == null)
            mLoadMode = FIRST_LOAD_MODE
        else
            mLoadMode = loadMode
        //使用OkHttpClient请求数据
        val client = OkHttpClient()
        val mUrl =
            HttpUrl.
            parse(NoticeApi.themeUrl)?.
            newBuilder()?.
            setQueryParameter("start",start.toString())?.
            setQueryParameter("num",num.toString())?.
            build()

        val request = Request.Builder().url(mUrl).get().build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
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

    private fun handleData(response: Response){
        val jsonString = response.body()?.string()
        val noticeInteractionBean = Gson().fromJson(jsonString,NoticeInteractionBean::class.java)
        val itemList = noticeInteractionBean.itemList
        val nextPageUrl = noticeInteractionBean.nextPageUrl
        mInteractionBean = noticeInteractionBean
        mItemList = itemList
        mNextPageUrl = nextPageUrl
        if(mLoadMode == FIRST_LOAD_MODE){
            mItemList = itemList
        }
        else if(mLoadMode == LOAD_MORE_MODE){
            mItemList.addAll(itemList)
            mRefreshAndLoadTarget.finishLoad()
        }
        else if(mLoadMode == REFRESH_MODE){
            mItemList = itemList
            mRefreshAndLoadTarget.finishRefresh()
        }
        else{
            Log.e("fun handleData","错误的加载模式。\n")
        }
    }

    fun setLoadingParam(start:Int?,num:Int?){
        if(start != null && start >=0){
            this.start = start
        }
        else
            Log.e("setLoadingParam","start参数格式错误。应为正整数。")
        if(num != null && num >0)
            this.num = num
        else
            Log.e("setLoadingParam","num参数格式错误。应为正整数。")
    }

    companion object{
        const val FIRST_LOAD_MODE = 0
        const val LOAD_MORE_MODE = 1
        const val REFRESH_MODE = 2
    }
}