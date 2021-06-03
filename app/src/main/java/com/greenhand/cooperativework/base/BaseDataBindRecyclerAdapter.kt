package com.greenhand.cooperativework.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * 一个联合了 DataBinding 的 RecyclerView.Adapter 的基类，内部默认实现了一个与 DataBinding 联合的 ViewHolder
 *
 * 普通使用时的方法如下所示：
 *
 * 1、[getYourItemViewType]--------> 用于分配自己的 ViewHolder
 *
 * 2、[onYourCreateViewHolder]-----> 用于设置自己的 ViewHolder
 *
 * 3、[onYourBindViewHolder]-------> 用于使用自己的 ViewHolder
 *
 * 4、[onBaseBindViewHolder]-------> 用于设置在类声明时传入的 DataBinding
 *
 * 还包括 RecyclerView.Adapter 的其他方法
 *
 * **NOTICE：** 如果你想加入自己的 ViewHolder，请在类声明时传入 [isAddYourViewHolder]
 *
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/27
 */
@Deprecated("因设计不合理，现在废除", ReplaceWith("请使用 BaseSimplifyRecyclerAdapter 代替"))
abstract class BaseDataBindRecyclerAdapter<DB : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,

    /**
     * 是否添加自己的 ViewHolder，填 true 后请使用
     *
     * 1、[getYourItemViewType]
     *
     * 2、[onYourCreateViewHolder]
     *
     * 3、[onYourBindViewHolder]
     *
     * 设置自己的 ViewHolder
     */
    private val isAddYourViewHolder: Boolean = false

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        /**
         * 用于 [getYourItemViewType]，代表内部与 DataBinding 联合的 ViewHolder 的 viewType 类型
         *
         * 具体值为 -2897483，不要故意重复该值
         */
        const val BASE_VIEW_HOLDER = -2897483 // 防止出现重复值
    }

    /**
     * **WARNING：** 如果你想加入你的 ViewHolder，请实现 [getYourItemViewType] 方法!
     *
     * **WARNING：** 禁止重写该方法，虽可以使用，但建议调用 [getYourItemViewType]
     * @see [getYourItemViewType]
     */
    @Deprecated("禁止重写该方法",
        ReplaceWith("getYourItemViewType()"))
    override fun getItemViewType(position: Int): Int {
        if (isAddYourViewHolder) {
            return getYourItemViewType(position)
        }
        return BASE_VIEW_HOLDER
    }

    /**
     * 用于分配自己的 ViewHolder
     *
     * **NOTICE：** 返回 [BASE_VIEW_HOLDER] 才可以设置成内部与 DataBinding 联合的 ViewHolder
     *
     * @return 返回 [BASE_VIEW_HOLDER] 即可设置成内部与 DataBinding 联合的 ViewHolder
     */
    open fun getYourItemViewType(position: Int): Int {
        return BASE_VIEW_HOLDER
    }

    /**
     * **WARNING：** 禁止重写该方法，该方法中有一些特殊的实现，如果你想加入你的 ViewHolder，请实现 [onYourCreateViewHolder] 方法!
     * @see [onYourCreateViewHolder]
     */
    @Deprecated("禁止重写该方法，该方法中有一些特殊的实现",
        ReplaceWith("onYourCreateViewHolder()"))
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isAddYourViewHolder) {
            if (viewType == BASE_VIEW_HOLDER) {
                BaseDataBindViewHolder(getBaseBinding(parent))
            }else {
                onYourCreateViewHolder(parent, viewType) ?:
                throw IllegalAccessException("如果你想加入你的 ViewHolder，请实现 onYourCreateViewHolder() 方法!")
            }
        }else {
            BaseDataBindViewHolder(getBaseBinding(parent))
        }
    }

    /**
     * 用于设置自己的 ViewHolder
     */
    open fun onYourCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return null
    }

    /**
     * **WARNING：** 禁止重写该方法，该方法中有一些特殊的实现，如果你想使用自己的 ViewHolder，请实现 [onYourBindViewHolder] 方法!
     * @see [onYourBindViewHolder]
     */
    @Deprecated("禁止重写该方法，该方法中有一些特殊的实现",
        ReplaceWith("onYourBindViewHolder()"))
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (getYourItemViewType(position) == BASE_VIEW_HOLDER) {
            val baseHolder = holder as BaseDataBindViewHolder
            val binding = baseHolder.binding as DB
            if (payloads.isEmpty()) {
                onBaseBindViewHolder(binding, baseHolder, position)
                binding.executePendingBindings()
            }else {
                onBaseBindViewHolder(binding, baseHolder, position, payloads)
            }
        }else {
            onYourBindViewHolder(holder, position, getYourItemViewType(position))
        }
    }

    /**
     * 用于使用自己的 ViewHolder
     *
     * **NOTICE：** 使用时请根据 [viewType] 强制转换 holder 为你自己的 ViewHolder
     * @param viewType 来自于 [getYourItemViewType]
     */
    open fun onYourBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, viewType: Int) {
        // nothing
    }

    /**
     * 用于设置在类声明时传入的 DataBinding
     *
     * 1、其中传入的是类声名时与 DataBinding 联合的 ViewHolder ----- [BaseDataBindViewHolder]
     *
     * 2、对 DataBinding 修改值后可以不用调用 dataBinding#executePendingBindings() 刷新，已经内部实现
     *
     * **WARNING：** 该方法只会在调用相关 notify 方法没有 payload 时才会被调用，如果有 payload，请使用另一个同名方法
     */
    abstract fun onBaseBindViewHolder(binding: DB, holder: BaseDataBindViewHolder, position: Int)

    /**
     * 用于设置在类声明时传入的 DataBinding
     *
     * **NOTICE：** 该方法只会在调用相关 notify 方法 ***拥有 payload*** 的传参时才会被调用
     * @see [onBaseBindViewHolder]
     */
    open fun onBaseBindViewHolder(binding: DB, holder: BaseDataBindViewHolder, position: Int, payloads: MutableList<Any>) {
        // nothing
    }

    /**
     * **WARNING：** 禁止实现该方法，该方法永远不会被调用，如果你想使用你的 ViewHolder，请实现 [onYourBindViewHolder] 方法!
     * @see [onYourBindViewHolder]
     */
    @Deprecated("该方法永远不会被调用",
        ReplaceWith("onYourBindViewHolder()"))
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 该方法永远不会被调用
        // 如果你加入了你的 ViewHolder，请使用 onYourBindViewHolder() 代替
    }

    private fun getBaseBinding(parent: ViewGroup): DB {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
    }

    class BaseDataBindViewHolder(
        val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root)
}