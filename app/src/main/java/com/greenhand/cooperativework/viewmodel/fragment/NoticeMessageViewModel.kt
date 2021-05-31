package com.greenhand.cooperativework.viewmodel.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenhand.cooperativework.bean.NoticeMessageBean
import com.greenhand.cooperativework.http.NoticeApi
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class NoticeMessageViewModel: ViewModel() {

    private var mMessageList = ArrayList<NoticeMessageBean>()
    val messageList by lazy{ MutableLiveData<MutableList<NoticeMessageBean>>() }

    fun loadData(){
        val client = OkHttpClient()
        val request = Request.Builder().url(NoticeApi.messageUrl).get().build()
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
        val messageListJsonArray = jsonObject.getJSONArray("messageList")
        val listCache = ArrayList<NoticeMessageBean>()
        for(index in 0 until messageListJsonArray.length()-1){
            val dataCache = messageListJsonArray.getJSONObject(index)
            val title= dataCache.getString("title")
            val content = dataCache.getString("content")
            val icon = dataCache.getString("icon")
            val viewed = dataCache.getBoolean("viewed")
            val date = dataCache.getInt("date")
            val actionUrl = dataCache.getString("actionUrl")

            val messageCache = NoticeMessageBean(title,content,date,icon,viewed,actionUrl)
            listCache.add(messageCache)
        }
        mMessageList.addAll(listCache)
    }


}