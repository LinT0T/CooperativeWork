package com.greenhand.cooperativework.base

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/31
 */
class BaseSimplifyRecyclerAdapter(
    private var itemCount: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * 创建 DataBinding 的回调
     *
     * DataBinding 会在回调中返回
     *
     * @param layoutId XML文件id
     * @param isInHere 类似于 getItemViewType()，会根据你的返回值设置是否是该 item 显示的位置
     * @param callback 类似于 onBindViewHolder()
     */
    fun <DB: ViewDataBinding> onBindView(@LayoutRes layoutId: Int,
                                         isInHere: (position: Int) -> Boolean,
                                         callback: (binding: DB, holder: BindingVH, position: Int) -> Unit): BaseSimplifyRecyclerAdapter {
        val call = BindingCallBack(
            layoutId,
            callback,
            isInHere,
        )
        for (i in 0 until itemCount) {
            if (isInHere(i)) {
                mPositionsWithCallback.put(i, call as BindingCallBack<*>)
            }
        }
        mLayoutIdWithCallback.put(layoutId, call as BindingCallBack<*>)
        return this
    }

    /**
     * 创建非 DataBinding 的回调
     *
     * 首先你要继承于 RecyclerView.ViewHolder 实现构造器为（View）的 ViewHolder
     *
     * @param layoutId XML文件id
     * @param viewHolderClass 你设置的 ViewHolder 的 Class 对象，**WARNING：** ViewHolder 的构造器必须为（View）
     * @param isInHere 类似于 getItemViewType()，会根据你的返回值设置是否是该 item 显示的位置
     * @param callback 类似于 onBindViewHolder()
     */
    fun <VH: RecyclerView.ViewHolder> onBindView(@LayoutRes layoutId: Int,
                                                 viewHolderClass: Class<VH>,
                                                 isInHere: (position: Int) -> Boolean,
                                                 callback: (holder: VH, position: Int) -> Unit): BaseSimplifyRecyclerAdapter {
        val call = CommonCallBack(
            layoutId,
            callback,
            viewHolderClass,
            isInHere,
        )
        for (i in 0 until itemCount) {
            if (isInHere(i)) {
                mPositionsWithCallback.put(i, call as CommonCallBack<*>)
            }
        }
        mLayoutIdWithCallback.put(layoutId, call as CommonCallBack<*>)
        return this
    }

    /**
     * 用于改变 itemCount
     *
     * **NOTE：** 填入增加量
     */
    fun addItemCount(addItemCount: Int) {
        for (i in 1..addItemCount) {
            mLayoutIdWithCallback.forEach { layoutId, callback ->
                if (callback.isInHere(itemCount + i)) {
                    mPositionsWithCallback.put(itemCount + i, callback)
                }
            }
        }
        itemCount += addItemCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, layoutId: Int): RecyclerView.ViewHolder {
        val callBack = mLayoutIdWithCallback.get(layoutId)
        return if (callBack is BindingCallBack<*>) {
            val binding = getBinding(callBack.layoutId, parent)
            BindingVH(binding)
        }else {
            val rootView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            (callBack as CommonCallBack<*>).getClass(rootView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val callBack = mPositionsWithCallback.get(position)
        if (holder is BindingVH) {
            callBack.invoke(holder, position, holder.binding)
            holder.binding.executePendingBindings()
        }else {
            callBack.invoke(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        val index = mPositionsWithCallback.indexOfKey(position)
        if (index >= 0) {
            return mPositionsWithCallback.valueAt(index).layoutId
        }
        return 0
    }

    private val mPositionsWithCallback = SparseArray<Callback>()
    private val mLayoutIdWithCallback = SparseArray<Callback>()

    private fun getBinding(@LayoutRes layoutId: Int, parent: ViewGroup): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
    }

    class BindingVH(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    private abstract class Callback(
        val layoutId: Int,
        val isInHere: (position: Int) -> Boolean,
    ) {
        abstract fun invoke(holder: RecyclerView.ViewHolder, position: Int, binding: ViewDataBinding? = null)
    }

    private class BindingCallBack<DB: ViewDataBinding>(
        layoutId: Int,
        private val onCallback: (binding: DB, holder: BindingVH, position: Int) -> Unit,
        isInHere: (position: Int) -> Boolean,
    ) : Callback(layoutId, isInHere) {
        override fun invoke(
            holder: RecyclerView.ViewHolder,
            position: Int,
            binding: ViewDataBinding?
        ) {
            onCallback.invoke(binding as DB, holder as BindingVH, position)
        }
    }

    private class CommonCallBack<VH: RecyclerView.ViewHolder>(
        layoutId: Int,
        private val onCallback: ((holder: VH, position: Int) -> Unit),
        val viewHolderClass: Class<VH>,
        isInHere: (position: Int) -> Boolean,
    ) : Callback(layoutId, isInHere) {
        override fun invoke(
            holder: RecyclerView.ViewHolder,
            position: Int,
            binding: ViewDataBinding?
        ) {
            onCallback.invoke(holder as VH, position)
        }

        fun getClass(itemView: View): VH {
            val constructor = viewHolderClass.getConstructor(View::class.java)
            return constructor.newInstance(itemView)
        }
    }
}