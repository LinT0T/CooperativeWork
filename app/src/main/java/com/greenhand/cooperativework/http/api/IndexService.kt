package com.greenhand.cooperativework.http.api

import com.greenhand.cooperativework.bean.IndexDiscoverBean
import retrofit2.Call
import retrofit2.http.GET

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/1
 */
interface IndexService {
    @GET("v7/index/tab/discovery?udid=fa53872206ed42e3857755c2756ab683fc22d64a&vc=591&vn=6.2.1&size=720X1280&deviceModel=Che1-CL20&first_channel=eyepetizer_zhihuiyun_market&last_channel=eyepetizer_zhihuiyun_market&system_version_code=19")
    fun getDiscoverBean(): Call<IndexDiscoverBean>
}