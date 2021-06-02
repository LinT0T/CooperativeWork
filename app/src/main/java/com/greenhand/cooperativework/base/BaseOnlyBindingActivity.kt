package com.greenhand.cooperativework.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/2
 */
abstract class BaseOnlyBindingActivity<DB: ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    /**
     * 是否锁定竖屏
     */
    isPortraitScreen: Boolean = true,

    /**
     * 是否沉浸式状态栏
     */
    isCancelStatusBar: Boolean = true
) : BaseActivity(isPortraitScreen, isCancelStatusBar) {

    val mBinding: DB by lazy {
        val binding = DataBindingUtil.setContentView<DB>(this, layoutId)
        binding.lifecycleOwner = this
        binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    abstract fun init()
}