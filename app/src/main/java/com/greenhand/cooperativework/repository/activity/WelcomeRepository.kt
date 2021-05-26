package com.greenhand.cooperativework.repository.activity

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import com.greenhand.cooperativework.base.BaseApplication
import com.greenhand.cooperativework.http.NetWork
import com.greenhand.cooperativework.utils.TimeUtil

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
object WelcomeRepository {

    private val share = BaseApplication.appContext.getSharedPreferences("welcome", MODE_PRIVATE)
    private const val imgBaseUrl = "https://cn.bing.com"

    suspend fun getImgUrl(): String? {
        return if (!isLoadLocalImgUrl()) {
            try {
                val imgUrlResponse = NetWork.getWelcomeImgUrlResponse()
                val url = imgBaseUrl + imgUrlResponse.images[0].url
                saveImgUrl(url)
                url
            }catch (e: Throwable) {
                Toast.makeText(BaseApplication.appContext, "网络连接失败！将使用本地图片", Toast.LENGTH_SHORT).show()
                return getLocalImgUrl()
            }
        }else {
            return getLocalImgUrl()
        }
    }

    private fun saveImgUrl(imgUrl: String) {
        val edit = share.edit()
        edit.putString("img_url", imgUrl)
        edit.apply()
    }

    private fun getLocalImgUrl(): String? {
        val imgUrl = share.getString("img_url", null)
        if (imgUrl == null) {
            Toast.makeText(BaseApplication.appContext, "本地图片失效 >_<", Toast.LENGTH_SHORT).show()
        }
        return imgUrl
    }

    private fun isLoadLocalImgUrl(): Boolean {
        val nowDate = TimeUtil.getNowDate()
        val lastLoadDate = share.getString("last_load_img_date", nowDate)
        return if (nowDate != lastLoadDate) {
            val edit = share.edit()
            edit.putString("last_load_img_date", nowDate)
            edit.apply()
            true
        }else false
    }
}