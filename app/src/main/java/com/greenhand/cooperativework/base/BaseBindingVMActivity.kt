package com.greenhand.cooperativework.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/2
 */
abstract class BaseBindingVMActivity<VM: ViewModel, DB: ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val modelClass: Class<VM>,
    /**
     * 是否锁定竖屏
     */
    isPortraitScreen: Boolean = true,

    /**
     * 是否沉浸式状态栏
     */
    isCancelStatusBar: Boolean = true
) : BaseOnlyBindingActivity<DB>(layoutId, isPortraitScreen, isCancelStatusBar) {

    val mViewModel by lazy {
        ViewModelProvider(this).get(modelClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }
}