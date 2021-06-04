package com.greenhand.cooperativework.weight

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/5
 */
class MyNestedScrollView(context: Context?, attrs: AttributeSet?) : ScrollView(context, attrs), NestedScrollingParent2 {

    private val mParentHelper = NestedScrollingParentHelper(this)

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        if (type == ViewCompat.TYPE_TOUCH) {
            if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
                return true
            }
        }
        return false
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        mParentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        mParentHelper.onStopNestedScroll(target, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        if (type == ViewCompat.TYPE_TOUCH) {
            scrollBy(0, dyUnconsumed)
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
    }
}