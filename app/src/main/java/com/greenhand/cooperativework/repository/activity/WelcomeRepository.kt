package com.greenhand.cooperativework.repository.activity

import android.content.Context.MODE_PRIVATE
import com.greenhand.cooperativework.base.BaseApplication
import com.greenhand.cooperativework.http.WelcomeNetWork
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
                val imgUrlResponse = WelcomeNetWork.getWelcomeImgUrlResponse()
                val url = imgBaseUrl + imgUrlResponse.images[0].url
                saveImgUrl(url)
                url
            }catch (e: Throwable) {
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
        return share.getString("img_url", null)
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