package com.greenhand.cooperativework.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
@BindingAdapter("imgFromUrl")
fun ImageView.imgFromUrl(url: String?) {
    Glide.with(this).load(url).into(this)
}