package com.greenhand.cooperativework.repository.fragment

import com.greenhand.cooperativework.bean.IndexDiscoverBean
import com.greenhand.cooperativework.http.IndexNetWork

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/1
 */
object IndexRepository {

    suspend fun getIndexDiscoverBean(): IndexDiscoverBean {
        return IndexNetWork.getIndexDiscoverResponse()
    }
}