package com.ndhzs.slideshow.layout

import android.content.Context
import android.view.MotionEvent
import android.view.View

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/28
 */
class IndicatorsView(context: Context) : View(context) {
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return false
    }

}