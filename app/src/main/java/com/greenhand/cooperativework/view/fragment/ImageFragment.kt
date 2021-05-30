package com.greenhand.cooperativework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.greenhand.cooperativework.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    private lateinit var dataBinding: FragmentImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //获取Binding 并绑定传入的数据
        dataBinding = FragmentImageBinding.inflate(layoutInflater)
        dataBinding.url = arguments?.getString("url")
        dataBinding.eventHandle = EventHandle()
        return dataBinding.root
    }

    /**
     * 事件内部类
     * 通过binding绑定到xml中
     */
    inner class EventHandle() {
        //处理单击事件
        fun onItemSingleClick(view: View) {
            //回调ImageDetailsActivity中方法 隐藏其他view
            (activity as ViewState?)?.hideOrShow()
        }
    }

    /**
     * 回调接口 ImageDetailsActivity中实现
     */
    interface ViewState {
        fun hideOrShow()
    }
}