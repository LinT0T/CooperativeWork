package com.greenhand.cooperativework.viewmodel.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenhand.cooperativework.bean.NoticeMessageBean
import com.greenhand.cooperativework.http.NoticeApi
import com.greenhand.cooperativework.utils.toast
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class NoticeMessageViewModel: ViewModel() {

    private var mMessageList = ArrayList<NoticeMessageBean>()
    private var nextPageUrl:String = UNDEFINED_NEXT_PAGE_URL
    private var dataFull:Boolean = false
    private var nextStartingMessageIndex:Int = 0
    private var messageDataPerLoading:Int = 10

    val messageList by lazy{ MutableLiveData<ArrayList<NoticeMessageBean>>() }

    /**
     * 通过loadData()方法可以向ViewModel中刷新数据以及添加下一页数据。
     */
    fun loadData(){
        if(dataFull){
            showDataFull()
            return
        }
        //创建OkHttpClient
        val client = OkHttpClient()
        val request = Request.Builder().get().url(NoticeApi.messageUrl).
        addHeader("start",nextStartingMessageIndex.toString()).
        addHeader("num",messageDataPerLoading.toString()).build()
        val call = client.newCall(request)
        call.enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("RequestError","Request failure!")
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                    retreatData(response)
                else
                    Log.e("RequestError","Request redirected!")
            }
        })
    }

    private fun retreatData(response:Response){
        val dataCache:String = response.body()!!.string()
        //这里的json数据比较简单，所以手动解析
        val jsonObject = JSONObject(dataCache)
        var nextPageUrlCache:String = UNDEFINED_NEXT_PAGE_URL
        val messageListJsonArray = jsonObject.getJSONArray("messageList")
        val listCache = ArrayList<NoticeMessageBean>()
        for(index in 0 until messageListJsonArray.length()-1){
            val dataCache = messageListJsonArray.getJSONObject(index)
            val title= dataCache.getString("title")
            val content = dataCache.getString("content")
            val icon = dataCache.getString("icon")
            val viewed = dataCache.getBoolean("viewed")
            val date = dataCache.getLong("date")
            val actionUrl = dataCache.getString("actionUrl")

            val messageCache = NoticeMessageBean(title,content,date,icon,viewed,actionUrl)
            listCache.add(messageCache)
        }


        nextPageUrlCache = jsonObject.getString("nextPageUrl")
        //检测是下一页是否为空，为空则说明数据已全部加载完毕。不是则继续记录下一页，等待刷新。
        if(nextPageUrl.isNullOrBlank()){
            nextPageUrl = DATA_FULL_NEXT_PAGE_URL
            dataFull = true
        }
        else{
            nextPageUrl = nextPageUrlCache
            nextStartingMessageIndex += messageDataPerLoading
        }
        mMessageList.addAll(listCache)
        messageList.postValue(mMessageList)
    }

    companion object {
        private const val UNDEFINED_NEXT_PAGE_URL:String = "UNDEFINED"
        private const val DATA_FULL_NEXT_PAGE_URL:String = "FULL"
    }

    private fun showDataFull(){
        "消息已经加载完毕了哦>_<".toast()
    }
}