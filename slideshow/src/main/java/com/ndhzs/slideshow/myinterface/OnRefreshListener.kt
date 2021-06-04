package com.ndhzs.slideshow.myinterface

import androidx.recyclerview.widget.RecyclerView

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/29
 */
fun interface OnRefreshListener {
    fun onRefresh(holder: RecyclerView.ViewHolder, position: Int)
}