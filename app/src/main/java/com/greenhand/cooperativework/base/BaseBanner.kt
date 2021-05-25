package com.greenhand.cooperativework.base

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.youth.banner.Banner

class BaseBanner : Banner {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true) //设置不拦截
        return super.dispatchTouchEvent(ev)
    }
}