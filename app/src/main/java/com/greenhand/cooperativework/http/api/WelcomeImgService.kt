package com.greenhand.cooperativework.http.api

import com.greenhand.cooperativework.bean.WelcomeImgBean
import retrofit2.Call
import retrofit2.http.GET

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
interface WelcomeImgService {
    @GET("HPImageArchive.aspx?format=js&idx=0&n=1")
    fun getImageData(): Call<WelcomeImgBean>
}