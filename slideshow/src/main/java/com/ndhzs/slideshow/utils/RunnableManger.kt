package com.ndhzs.slideshow.utils

import android.view.View
import kotlin.collections.HashSet

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/29
 */
class RunnableManger(private val view: View) {

    private val hashSet = HashSet<Runnable>()

    fun post(runnable: Runnable) {
        hashSet.add(runnable)
        view.post(runnable)
    }

    fun postDelay(delayMillis: Long, runnable: Runnable) {
        hashSet.add(runnable)
        view.postDelayed(runnable, delayMillis)
    }

    fun remove(runnable: Runnable): Boolean {
        view.removeCallbacks(runnable)
        return hashSet.remove(runnable)
    }

    fun destroy() {
        hashSet.forEach {
            view.removeCallbacks(it)
        }
        hashSet.clear()
    }
}