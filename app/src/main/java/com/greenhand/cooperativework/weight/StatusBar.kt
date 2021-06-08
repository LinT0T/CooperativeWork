package com.greenhand.cooperativework.weight

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/8
 */
class StatusBar(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    var mStatusBarHeight = 0
    init {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId != 0) {
            mStatusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newHeightMS = MeasureSpec.makeMeasureSpec(mStatusBarHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, newHeightMS)
    }
}