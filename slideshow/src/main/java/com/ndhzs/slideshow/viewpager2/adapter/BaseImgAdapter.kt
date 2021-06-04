package com.ndhzs.slideshow.viewpager2.adapter

import android.util.SparseArray
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.ndhzs.slideshow.myinterface.OnImgRefreshListener
import com.ndhzs.slideshow.utils.SlideShowAttrs
import com.ndhzs.slideshow.utils.Refresh

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/27
 */
abstract class BaseImgAdapter<T> : RecyclerView.Adapter<BaseImgAdapter.BaseImgViewHolder>() {

    private lateinit var oldData: List<T>
    private val datas = ArrayList<T>()
    private lateinit var attrs: SlideShowAttrs
    private val array = SparseArray<ConditionWithListener>()
    private var mIsCirculate = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseImgViewHolder {
        val imageView = ShapeableImageView(parent.context)
        val lp = FrameLayout.LayoutParams(attrs.imgWidth, attrs.imgHeight)
        lp.gravity = Gravity.CENTER
        lp.leftMargin = attrs.imgMarginHorizontal
        lp.topMargin = attrs.imgMarginVertical
        lp.rightMargin = attrs.imgMarginHorizontal
        lp.bottomMargin = attrs.imgMarginVertical
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setTopLeftCornerSize(attrs.imgLeftTopRadius)
                .setTopRightCornerSize(attrs.imgRightTopRadius)
                .setBottomLeftCornerSize(attrs.imgLeftBottomRadius)
                .setBottomRightCornerSize(attrs.imgRightBottomRadius)
                .build()
        imageView.setBackgroundColor(attrs.imgDefaultColor)
        val frameLayout = FrameLayout(parent.context)
        val lpFl = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frameLayout.layoutParams = lpFl
        frameLayout.addView(imageView, lp)
        frameLayout.setBackgroundColor(0x00000000)
        parent.setBackgroundColor(0x00000000)
        return BaseImgViewHolder(frameLayout)
    }

    /**
     * **WARNING：** 因为该方法实现了接口刷新，请看 SlideShow#notifyImageViewRefresh，所以禁止重写
     * @see [onBindImageView]
     */
    @Deprecated("禁止重写! ", ReplaceWith("onBindImageView"))
    override fun onBindViewHolder(
        holder: BaseImgViewHolder,
        realPosition: Int,
        payloads: MutableList<Any>
    ) {
        var falsePosition = realPosition
        if (mIsCirculate) {
            if (realPosition <= 1) {
                falsePosition += datas.size - 4
            }else if (realPosition >= datas.size - 2) {
                falsePosition -= datas.size - 4
            }
            falsePosition -= 2
        }
        if (array.indexOfKey(falsePosition) >= 0) {
            val conditionWithListener = array[falsePosition]
            when (conditionWithListener.condition) {
                Refresh.Condition.COEXIST -> {
                    onBindViewHolder(holder, falsePosition)
                    conditionWithListener.l.onRefresh(holder.imageView, holder, falsePosition)
                }
                Refresh.Condition.COVERED -> {
                    conditionWithListener.l.onRefresh(holder.imageView, holder, falsePosition)
                }
                Refresh.Condition.ONLY_ONE -> {
                    payloads.forEach {
                        if (it == BaseRecyclerAdapter.ITEM_REFRESH) {
                            array[falsePosition].l.onRefresh(holder.imageView, holder, falsePosition)
                            if (array[falsePosition].condition == Refresh.Condition.ONLY_ONE) {
                                array.remove(falsePosition)
                            }
                        }
                    }
                }
            }
        }else if (payloads.isEmpty()) {
            onBindViewHolder(holder, falsePosition)
        }
    }

    /**
     * **WARNING：** 因为该方法有特殊实现，所以禁止重写
     * @see [onBindImageView]
     */
    @Deprecated("禁止重写! ", ReplaceWith("onBindImageView"))
    override fun onBindViewHolder(holder: BaseImgViewHolder, falsePosition: Int) {
        onBindImageView(oldData[falsePosition], holder.imageView, holder, falsePosition)
    }

    /**
     * **WARNING：** 请不要重写
     */
    @Deprecated("禁止重写! ")
    override fun getItemCount(): Int {
        return datas.size
    }

    /**
     * **WARNING：** 请不要自己调用
     */
    @Deprecated("禁止自己调用! ")
    fun setData(datas: List<T>, attrs: SlideShowAttrs) {
        this.oldData = datas
        this.datas.addAll(datas)
        this.attrs = attrs
    }

    /**
     * 在修改了外部数组后可以调用该方法来刷新
     */
    fun refreshData() {
        this.datas.clear()
        this.datas.addAll(oldData)
        if (mIsCirculate) {
            mIsCirculate = false
            openCirculateEnabled()
        }
        notifyDataSetChanged()
    }

    /**
     * **WARNING：** 请不要自己调用
     */
    @Deprecated("禁止自己调用! ")
    fun refreshData(datas: List<T>) {
        this.oldData = datas
        this.datas.clear()
        this.datas.addAll(datas)
        if (mIsCirculate) {
            mIsCirculate = false
            openCirculateEnabled()
        }
        notifyDataSetChanged()
    }

    /**
     * **WARNING：** 请不要自己调用
     */
    @Deprecated("禁止自己调用! ")
    fun openCirculateEnabled() {
        if (!mIsCirculate) {
            if (datas.size > 1) {
                mIsCirculate = true
                val size = datas.size
                val newList = ArrayList<T>()
                newList.add(datas[size - 2])
                newList.add(datas[size - 1])
                newList.addAll(datas)
                newList.add(datas[0])
                newList.add(datas[1])
                datas.clear()
                datas.addAll(newList)
            }
        }
    }

    /**
     * **WARNING：** 请不要自己调用
     */
    fun setImgRefreshListener(falsePosition: Int,
                              @Refresh.Condition
                              condition: Int,
                              l: OnImgRefreshListener) {
        array.put(falsePosition, ConditionWithListener(condition, l))
        if (mIsCirculate) { // 判断刷新的位置，如果开启循环就要进行计算
            val realPosition = falsePosition + 2
            if (realPosition <= 3) {
                notifyItemChanged(realPosition + datas.size - 4, BaseRecyclerAdapter.ITEM_REFRESH)
            }
            if (realPosition >= datas.size - 4) {
                notifyItemChanged(realPosition - (datas.size - 4), BaseRecyclerAdapter.ITEM_REFRESH)
            }
            notifyItemChanged(realPosition, BaseRecyclerAdapter.ITEM_REFRESH)
        }else {
            notifyItemChanged(falsePosition, BaseRecyclerAdapter.ITEM_REFRESH)
        }
    }

    fun removeImgRefreshListener(position: Int) {
        return array.remove(position)
    }

    fun clearImgRefreshListener() {
        return array.clear()
    }

    abstract fun onBindImageView(data: T, imageView: ShapeableImageView, holder: BaseImgViewHolder, position: Int)

    class BaseImgViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.getChildAt(0) as ShapeableImageView
    }

    class ConditionWithListener(
            val condition: Int,
            val l: OnImgRefreshListener
    )
}