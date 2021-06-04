package com.ndhzs.slideshow.viewpager2.adapter

import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView
import com.ndhzs.slideshow.myinterface.OnRefreshListener
import com.ndhzs.slideshow.utils.Refresh

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/29
 */
abstract class BaseRecyclerAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private val array = SparseArray<ConditionWithListener>()

    /**
     * 调用 SlideView#notifyRefresh 后，因为 notifyItemChanged 传入了 payload，所以该方法会被调用
     */
    @Deprecated("利用接口刷新在该方法中实现! 重写时请注意!", ReplaceWith(""))
    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (array.contains(position)) {
            val conditionWithListener = array.get(position)
            when (conditionWithListener.condition) {
                Refresh.Condition.COEXIST -> {
                    onBindViewHolder(holder, position)
                    conditionWithListener.l.onRefresh(holder, position)
                }
                Refresh.Condition.COVERED -> {
                    conditionWithListener.l.onRefresh(holder, position)
                }
                Refresh.Condition.ONLY_ONE -> {
                    payloads.forEach {
                        if (it == ITEM_REFRESH) {
                            array[position].l.onRefresh(holder, position)
                            if (array[position].condition == Refresh.Condition.ONLY_ONE) {
                                array.remove(position)
                            }
                        }
                    }
                }
            }
        }else if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        }
    }

    /**
     * **WARNING：** 请不要自己调用
     */
    fun setRefreshListener(
        position: Int,
        @Refresh.Condition
        condition: Int,
        l: OnRefreshListener,
    ) {
        array.put(position, ConditionWithListener(condition, l))
        notifyItemChanged(position, ITEM_REFRESH)
    }

    fun removeRefreshListener(position: Int) {
        return array.remove(position)
    }

    fun clearRefreshListener() {
        return array.clear()
    }

    class ConditionWithListener(
        val condition: Int,
        val l: OnRefreshListener,
    )

    companion object {

        /**
         * 表示刷新 item
         */
        internal const val ITEM_REFRESH = 0
    }
}