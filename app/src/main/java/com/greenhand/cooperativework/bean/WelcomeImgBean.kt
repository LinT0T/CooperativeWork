package com.greenhand.cooperativework.bean

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
data class WelcomeImgBean(val images: List<ImagesBean>) {
    data class ImagesBean(val url: String)
}