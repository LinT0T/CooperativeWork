package com.ndhzs.slideshow.viewpager2.transformer2

import android.view.View
import com.ndhzs.slideshow.viewpager2.transformer2.ABaseTransformer

open class ScaleInOutTransformer : ABaseTransformer() {

    override fun onTransform(page: View, position: Float) {
        page.pivotX = (if (position < 0) 0 else page.width).toFloat()
        page.pivotY = page.height / 2f
        val scale = if (position < 0) 1f + position else 1f - position
        page.scaleX = scale
        page.scaleY = scale
    }
}
