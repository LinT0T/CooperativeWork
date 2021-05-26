package com.greenhand.cooperativework.bean

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
data class WelcomeImgBean(val images: List<ImagesBean>, val tooltips: TooltipsBean) {

    data class ImagesBean(val startdate: String,
                          val fullstartdate: String,
                          val enddate: String,
                          val url: String,
                          val urlbase: String,
                          val copyright: String,
                          val copyrightlink: String,
                          val title: String,
                          val quiz: String,
                          val wp: Boolean,
                          val hsh: String,
                          val drk: Int,
                          val top: Int,
                          val bot: Int,
                          val hs: List<Any?>)

    data class TooltipsBean(val loading: String,
                            val previous: String,
                            val next: String,
                            val walle: String,
                            val walls: String)
}