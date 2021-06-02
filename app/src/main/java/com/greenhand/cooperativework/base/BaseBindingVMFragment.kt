package com.greenhand.cooperativework.base

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * .....
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/1
 */
abstract class BaseBindingVMFragment<VM: ViewModel, DB: ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val modelClass: Class<VM>,
) : BaseOnlyBindingFragment<DB>(layoutId) {
    val mViewModel by lazy {
        ViewModelProvider(this).get(modelClass)
    }
}