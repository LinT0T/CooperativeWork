package com.greenhand.cooperativework.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
@BindingAdapter("imgFromUrl")
fun ImageView.imgFromUr(url: String?) {
    Glide
        .with(this)
        .load(url)
        .into(this)
}
